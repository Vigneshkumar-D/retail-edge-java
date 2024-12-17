package com.retailedge.service.supplier;

import com.retailedge.dto.supplier.PurchaseProductDto;
import com.retailedge.entity.suppiler.PurchaseProduct;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.supplier.PurchaseProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseProductService {

    @Autowired
    private PurchaseProductRepository purchaseProductRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<PurchaseProduct> list(){
        return purchaseProductRepository.findAll();
    }

    public PurchaseProduct add(PurchaseProductDto purchaseProductDto){
        PurchaseProduct purchaseProduct = modelMapper.map(purchaseProductDto, PurchaseProduct.class);
        return purchaseProductRepository.save(purchaseProduct);
    }

    public PurchaseProduct update(Integer purchaseProductId, PurchaseProductDto purchaseProductDto){
        PurchaseProduct purchaseProduct = purchaseProductRepository.findById(purchaseProductId).orElse(null);
        if (purchaseProduct == null) {
            throw new RuntimeException("Purchase Product Details not found with id: " + purchaseProductId);
        }
        modelMapper.map(purchaseProductDto, purchaseProduct);
        return purchaseProductRepository.save(purchaseProduct);
    }



    public ResponseEntity<ResponseModel<?>> delete(Integer purchaseProductId) throws Exception {

        try {
            if (!purchaseProductRepository.existsById(purchaseProductId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Purchase product not found", 404));
            }
            purchaseProductRepository.deleteById(purchaseProductId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Purchase Product: " + e.getMessage(), 500));
        }
    }
}
