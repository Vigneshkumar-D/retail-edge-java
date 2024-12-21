package com.retailedge.service.supplier;


import com.retailedge.dto.supplier.PaymentDetailsDto;
import com.retailedge.dto.supplier.PurchaseOrderDto;
import com.retailedge.dto.supplier.PurchaseProductDto;
import com.retailedge.entity.suppiler.PaymentDetails;
import com.retailedge.entity.suppiler.PurchaseOrder;
import com.retailedge.entity.suppiler.PurchaseProduct;
import com.retailedge.entity.suppiler.Supplier;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.supplier.PurchaseOrderRepository;
import com.retailedge.repository.supplier.SupplierRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @Autowired
    private PurchaseProductService purchaseProductService;

    @Autowired
    private SupplierRepository supplierRepository;

//    @Transactional
//    public PurchaseOrder add(PurchaseOrderDto purchaseOrderDto){
//        PurchaseOrder purchaseOrder = modelMapper.map(purchaseOrderDto, PurchaseOrder.class);
//
//        Supplier supplier = supplierRepository.findById(purchaseOrderDto.getSupplierId()).orElse(null);
//        if(supplier!= null){
//            supplier.setTotalOrderValue(supplier.getTotalOrderValue()+purchaseOrderDto.getOrderTotal());
//            supplier.setBalance(supplier.getBalance()+purchaseOrderDto.getOrderTotal());
//            supplierRepository.save(supplier);
//        }
//
//        List<PurchaseProductDto> purchaseProductDtoList =  purchaseOrderDto.getPurchaseProductDto();
//        List<PurchaseProduct> purchaseProductList = new ArrayList<>();
//        if(!purchaseProductDtoList.isEmpty()){
//            for(PurchaseProductDto purchaseProductDto: purchaseProductDtoList){
//                PurchaseProduct purchaseProduct = modelMapper.map(purchaseProductDto, PurchaseProduct.class);
//                purchaseProductList.add(purchaseProduct);
//            }
//        }
//
//        purchaseOrder.setPurchaseProducts(purchaseProductList);
//        PurchaseOrder purchaseOrder1 = purchaseOrderRepository.save(purchaseOrder);
//
//        return purchaseOrder1;
//    }
//
//
////    @Transactional
////    public PurchaseOrder add(PurchaseOrderDto purchaseOrderDto) {
////        // Retrieve the supplier
////        Supplier supplier = supplierRepository.findById(purchaseOrderDto.getSupplierId())
////                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
////
////        // Map PurchaseOrderDto to PurchaseOrder entity
////        PurchaseOrder purchaseOrder = modelMapper.map(purchaseOrderDto, PurchaseOrder.class);
////        purchaseOrder.setSupplier(supplier);
////
////        // Convert PurchaseProductDto list to PurchaseProduct entities
////        List<PurchaseProduct> purchaseProducts = new ArrayList<>();
////        for (PurchaseProductDto productDto : purchaseOrderDto.getPurchaseProductDto()) {
////            PurchaseProduct purchaseProduct = modelMapper.map(productDto, PurchaseProduct.class);
////            purchaseProduct.setPurchaseOrderId(purchaseOrder); // Set the relationship
////            purchaseProducts.add(purchaseProduct);
////        }
////
////        // Set products to purchaseOrder
////        purchaseOrder.setPurchaseProducts(purchaseProducts);
////
////        // Save the purchase order with products
////        return purchaseOrderRepository.save(purchaseOrder);
////    }
//

//    @Transactional
//    public PurchaseOrder add(PurchaseOrderDto purchaseOrderDto) {
//        // Map PurchaseOrderDto to PurchaseOrder entity
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
//            return conditions.getSource() != null;
//        });
//        PurchaseOrder purchaseOrder = modelMapper.map(purchaseOrderDto, PurchaseOrder.class);
//
//        // Retrieve the supplier and update Order Total, Balance, and Last Payment
//        Supplier supplier = supplierRepository.findById(purchaseOrderDto.getSupplierId())
//                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));
//
//        supplier.setTotalOrderValue(supplier.getTotalOrderValue() + purchaseOrderDto.getOrderTotal());
//        supplier.setBalance(supplier.getBalance() + purchaseOrderDto.getOrderTotal());
//        supplierRepository.save(supplier);
//
//        // Convert PurchaseProductDto list to PurchaseProduct entities and set the purchaseOrderId
//        List<PurchaseProduct> purchaseProductList = new ArrayList<>();
//        for (PurchaseProductDto productDto : purchaseOrderDto.getPurchaseProductDto()) {
//            PurchaseProduct purchaseProduct = modelMapper.map(productDto, PurchaseProduct.class);
//            purchaseProduct.setPurchaseOrderId(purchaseOrder); // Set the relationship
//            purchaseProductList.add(purchaseProduct);
//        }
//
//        // Set products to purchaseOrder
//        purchaseOrder.setPurchaseProducts(purchaseProductList);
//
//        // Save the purchase order with products
//        return purchaseOrderRepository.save(purchaseOrder);
//    }
//



    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list() {
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, purchaseOrderRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving purchase order details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    @Transactional
    public ResponseEntity<ResponseModel<?>> add(PurchaseOrderDto purchaseOrderDto) {
        try{
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.getConfiguration().setPropertyCondition(conditions -> conditions.getSource() != null);

            // Map PurchaseOrderDto to PurchaseOrder entity
            PurchaseOrder purchaseOrder = modelMapper.map(purchaseOrderDto, PurchaseOrder.class);

            // Retrieve the supplier
            Supplier supplier = supplierRepository.findById(purchaseOrderDto.getSupplier().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

            // Set the supplier for the PurchaseOrder
            purchaseOrder.setSupplier(supplier);

            // Update Supplier fields
            supplier.setTotalOrderValue(supplier.getTotalOrderValue() + purchaseOrderDto.getOrderTotal());
            supplier.setBalance(supplier.getBalance() + purchaseOrderDto.getOrderTotal());
            supplierRepository.save(supplier);

            // Convert PurchaseProductDto list to PurchaseProduct entities and set the purchaseOrder relationship
            List<PurchaseProduct> purchaseProductList = new ArrayList<>();
            for (PurchaseProductDto productDto : purchaseOrderDto.getPurchaseProducts()) {
                PurchaseProduct purchaseProduct = modelMapper.map(productDto, PurchaseProduct.class);
                purchaseProduct.setPurchaseOrderId(purchaseOrder); // Set the relationship
                purchaseProductList.add(purchaseProduct);
            }
            purchaseOrder.setOrderDate(Instant.now());
            purchaseOrder.setPurchaseProducts(purchaseProductList);

            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, purchaseOrderRepository.save(purchaseOrder)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving purchase order details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Integer purchaseOrderId, PurchaseOrderDto purchaseOrderDto) {
        try{
            PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId).orElse(null);
            if (purchaseOrder == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Purchase Order Details not found", 500));
            }
            modelMapper.map(purchaseOrderDto, purchaseOrder);

            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, purchaseOrderRepository.save(purchaseOrder)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating purchase order details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer purchaseOrderId) throws Exception {

        try {
            if (!purchaseOrderRepository.existsById(purchaseOrderId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Purchase order not found", 404));
            }
            purchaseOrderRepository.deleteById(purchaseOrderId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Purchase order: " + e.getMessage(), 500));
        }
    }


    public ResponseEntity<ResponseModel<?>> getLastThreeOrdersBySupplierId(Integer supplierId) {
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, purchaseOrderRepository.findTop3BySupplierIdOrderByOrderDateDesc(supplierId)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving purchase order details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }

    }
}
