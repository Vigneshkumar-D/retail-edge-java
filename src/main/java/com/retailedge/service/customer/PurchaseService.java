package com.retailedge.service.customer;

import com.retailedge.dto.customer.PurchaseDto;
import com.retailedge.entity.customer.Purchase;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.PurchaseRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
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

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, purchaseRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving purchase: " +exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> add(PurchaseDto purchaseDto){
        try{
            Purchase purchase = new Purchase();
            modelMapper.map(purchaseDto, purchase);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, purchaseRepository.save(purchase)));

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding purchase: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Integer purchaseId, PurchaseDto purchaseDto){
        try{
            Optional<Purchase> purchaseOptional = purchaseRepository.findById(purchaseId);
            if (purchaseOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Purchase Details not found with id: " + purchaseId, 500));

            }
            Purchase purchase = purchaseOptional.get();
            modelMapper.map(purchaseDto, purchase);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, purchaseRepository.save(purchase)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating purchase: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }



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
                    .body(new ResponseModel<>(false, "Error deleting Purchase: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> findByCustomer(Integer invoiceId) {
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, purchaseRepository.findByInvoiceId(invoiceId)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding purchase: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }

    }
}
