package com.retailedge.service.customer;

import com.retailedge.dto.customer.PurchaseDto;
import com.retailedge.entity.customer.Purchase;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.PurchaseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;


    @Autowired
    private ModelMapper modelMapper;

    public List<Purchase> list(){
        return purchaseRepository.findAll();
    }

    public Purchase add(PurchaseDto purchaseDto){
        Purchase purchase = new Purchase();
        modelMapper.map(purchaseDto, purchase);
        return purchaseRepository.save(purchase);
    }

    public Purchase update(Integer purchaseId, PurchaseDto purchaseDto){
        Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseId);

        if (!purchaseOptional.isPresent()) {
            throw new RuntimeException("Purchase Details not found with id: " + purchaseId);
        }

        Purchase purchase = purchaseOptional.get();

        modelMapper.map(purchaseDto, purchase);

        return purchaseRepository.save(purchase);
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer purchaseId) throws Exception {
        try {
            if (!purchaseRepository.existsById(purchaseId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Purchase not found", 404));
            }
            purchaseRepository.deleteById(purchaseId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Purchase: " + e.getMessage(), 500));
        }
    }


    public List<Purchase> findByCustomer(Integer invoiceId) {

        return purchaseRepository.findByInvoiceId(invoiceId);
    }
}
