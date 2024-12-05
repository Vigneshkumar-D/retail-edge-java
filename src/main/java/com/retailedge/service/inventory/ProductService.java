package com.retailedge.service.inventory;

import com.retailedge.controller.alert.NotificationController;
import com.retailedge.entity.gst.HSNCode;
import com.retailedge.entity.inventory.Category;
import com.retailedge.entity.inventory.Product;
import com.retailedge.dto.inventory.ProductDto;
//import com.retailedge.entity.product.MobileProduct;
import com.retailedge.entity.inventory.NonMobileProduct;
import com.retailedge.kafka.KafkaConsumerService;
import com.retailedge.model.ResponseModel;
import com.retailedge.model.inventory.BarcodeResult;
import com.retailedge.repository.gst.HSNCodeRepository;
import com.retailedge.repository.inventory.CategoryRepository;
import com.retailedge.repository.inventory.ProductRepository;
import com.retailedge.utils.inventory.BarcodeUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private  ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private HSNCodeRepository hsnCodeRepository;

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    public List<Product> list(){
        List<Product> productList = productRepository.findAll();
        if(productList.isEmpty()){
            kafkaConsumerService.consumeLowStockAlert("Low stock");
            logger.info("Notification sent successfully: {}", "Low stock alert for products");
        }
        return productRepository.findAll();
    }

    public ResponseEntity<ResponseModel<?>> add(ProductDto productDto) {

        Category category = categoryRepository.findById(productDto.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + productDto.getCategory()));

        if (productDto.getImeiNumber() != null) {
            Product product = modelMapper.map(productDto, Product.class);
            product.setCategory(category);
            HSNCode hsnCode =  hsnCodeRepository.findByTaxSlab_Category_Id(category.getId());
            if(hsnCode==null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(true, "No HSN code found for category: "+category.getCategory()+" Please add one before adding product!", 500));
            }else {
                product.setHsnCode(hsnCode);
                productRepository.save(product);
                return ResponseEntity.ok(new ResponseModel<>(true, "Added successfully", 200, productRepository.save(product)));
            }
        } else {
            // Handle NonMobileProduct creation
            NonMobileProduct nonMobileProduct = new NonMobileProduct();
            modelMapper.map(productDto, nonMobileProduct);

            // Generate barcode for non-mobile products
            BarcodeResult barcodeResult = BarcodeUtil.generateBarcode(nonMobileProduct.getProductName(), nonMobileProduct.getModel());
            assert barcodeResult != null;
            nonMobileProduct.setBarcode(barcodeResult.getBarcodeValue());
            nonMobileProduct.setBarcodeImage(barcodeResult.getBarcodeImage());
            nonMobileProduct.setBarcodeImagePath("/barcodes/product_" + nonMobileProduct.getProductName() + "_" + nonMobileProduct.getModel() + ".png");

            // Set the existing category
            nonMobileProduct.setCategory(category);

            HSNCode hsnCode =  hsnCodeRepository.findByTaxSlab_Category_Id(category.getId());

            if(hsnCode==null){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "No HSN code found for category: "+category.getCategory()+" Please add one before adding product !", 500));
            }else {
                nonMobileProduct.setHsnCode(hsnCode);
              return ResponseEntity.ok(new ResponseModel<>(true, "Added successfully", 200, productRepository.save(nonMobileProduct)));

            }
        }
    }


    public Product update(Integer productId, ProductDto productDto) {
        Optional<Product> productOptional = productRepository.findById(productId);

        if (!productOptional.isPresent()) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        Product product = productOptional.get();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
            return conditions.getSource() != null;
        });
        modelMapper.map(productDto, product);


        return productRepository.save(product);
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer productId) throws Exception {
        try {
            if (!productRepository.existsById(productId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Product not found", 404));
            }
            productRepository.deleteById(productId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Product: " + e.getMessage(), 500));
        }
    }

    public void bulkUpload(MultipartFile file) throws Exception {
        List<Product> productList = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            Map<String, Integer> headerMap = new HashMap<>();

            Row headerRow = sheet.getRow(0);
            for (Cell cell : headerRow) {
                headerMap.put(cell.getStringCellValue().toLowerCase(), cell.getColumnIndex());
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String name = row.getCell(headerMap.get("Name")).getStringCellValue();
                Double actualPrice = row.getCell(headerMap.get("Actual Price")).getNumericCellValue();
                Double sellingPrice = row.getCell(headerMap.get("Selling Price")).getNumericCellValue();
                String categoryName = row.getCell(headerMap.get("Category")).getStringCellValue();
                int stockLevel = (int) row.getCell(headerMap.get("Stock Level")).getNumericCellValue();
                String brand = row.getCell(headerMap.get("Brand")).getStringCellValue();
                String model = row.getCell(headerMap.get("Model")).getStringCellValue();
                BigDecimal taxRate = BigDecimal.valueOf(row.getCell(headerMap.get("Tax Rate")).getNumericCellValue());
                String barcode = row.getCell(headerMap.get("Barcode")).getStringCellValue();
                int lowStockThreshold = (int) row.getCell(headerMap.get("Low Stock Threshold")).getNumericCellValue();

                Category category = categoryRepository.findByCategory(categoryName);
                if (category == null) {
                    throw new Exception("Category not found: " + categoryName);
                }

                Product product = new Product();
                product.setProductName(name);
                product.setActualPrice(actualPrice);
                product.setSellingPrice(sellingPrice);
                product.setCategory(category);
                product.setStockLevel(stockLevel);
                product.setBrand(brand);
                product.setModel(model);
//              product.setBarcode(barcode);
                product.setLowStockThreshold(lowStockThreshold);

                productList.add(product);
            }
        } catch (Exception e) {
            throw new Exception("Error processing Excel file", e);
        }
        productRepository.saveAll(productList);
    }

    public void updateProductQuantity(Product product, Integer quantity) {
        Optional<Product> existingProduct = productRepository.findById(Math.toIntExact(product.getId()));
        if(existingProduct.isPresent()){
            Integer updatedStockLevel = existingProduct.get().getStockLevel();
            existingProduct.get().setStockLevel(updatedStockLevel-quantity);
            productRepository.save(existingProduct.get());
        }
    }
}





