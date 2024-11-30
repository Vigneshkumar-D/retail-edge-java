package com.retailedge.service.supplier;

import com.retailedge.dto.supplier.PaymentDetailsDto;
import com.retailedge.dto.supplier.PurchaseProductDto;
import com.retailedge.entity.suppiler.PaymentDetails;
import com.retailedge.entity.suppiler.PurchaseOrder;
import com.retailedge.entity.suppiler.Supplier;
import com.retailedge.enums.invoice.PaymentStatus;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.supplier.PaymentDetailsRepository;
import com.retailedge.repository.supplier.PurchaseOrderRepository;
import com.retailedge.repository.supplier.SupplierRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
        
@Service
public class PaymentDetailsService {

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    public List<PaymentDetails> list(){
        return paymentDetailsRepository.findAll();
    }

    public PaymentDetails add(PaymentDetailsDto paymentDetailsDto){
        PaymentDetails paymentDetails = modelMapper.map(paymentDetailsDto, PaymentDetails.class);
        Supplier supplier = supplierRepository.findById(Math.toIntExact(paymentDetailsDto.getSupplier().getId())).orElse(null);
        paymentDetails.setSupplier(supplier);

        if(supplier!= null && paymentDetailsDto.getPaymentStatus().equalsIgnoreCase("COMPLETED")){
            supplier.setPaidTotal(supplier.getPaidTotal()+paymentDetailsDto.getPaymentAmount());
            supplier.setBalance(supplier.getTotalOrderValue() - supplier.getPaidTotal());
            supplier.setLastPayment(paymentDetailsDto.getPaymentAmount());
            supplierRepository.save(supplier);
        }
        return paymentDetailsRepository.save(paymentDetails);
    }

//    public PaymentDetails update(Integer paymentDetailsId, PaymentDetailsDto paymentDetailsDto){
//        PaymentDetails paymentDetails = paymentDetailsRepository.findById(paymentDetailsId).orElse(null);
//        if (paymentDetails == null) {
//            throw new RuntimeException("Payment Details not found with id: " + paymentDetailsId);
//        }
//
//        Supplier supplier = supplierRepository.findById(Math.toIntExact(paymentDetailsDto.getSupplier().getId()))
//                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + paymentDetailsDto.getSupplier().getId()));
//        paymentDetails.setSupplier(supplier);
//
//
//        if (paymentDetailsDto.getPaymentStatus().equalsIgnoreCase(String.valueOf(PaymentStatus.COMPLETED))) {
//            supplier.setPaidTotal(supplier.getPaidTotal()+paymentDetailsDto.getPaymentAmount());
//            supplier.setBalance(supplier.getTotalOrderValue() - supplier.getPaidTotal());
//            supplier.setLastPayment(paymentDetailsDto.getPaymentAmount());
//        }
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
//            return conditions.getSource() != null;
//        });
//
//        modelMapper.map(paymentDetailsDto, paymentDetails);
//        return paymentDetailsRepository.save(paymentDetails);
//    }


    @Transactional
    public PaymentDetails update(Integer paymentDetailsId, PaymentDetailsDto paymentDetailsDto) {
        // Fetch the existing PaymentDetails to update
        PaymentDetails paymentDetails = paymentDetailsRepository.findById(paymentDetailsId)
                .orElseThrow(() -> new RuntimeException("Payment Details not found with id: " + paymentDetailsId));

        // Fetch the Supplier entity
        Supplier supplier = supplierRepository.findById(Math.toIntExact(paymentDetailsDto.getSupplier().getId()))
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + paymentDetailsDto.getSupplier().getId()));

        if ("COMPLETED".equalsIgnoreCase(paymentDetails.getPaymentStatus())
            && "PENDING".equalsIgnoreCase(paymentDetailsDto.getPaymentStatus())) {
            Double difference = paymentDetails.getPaymentAmount() - paymentDetailsDto.getPaymentAmount();
            supplier.setPaidTotal(supplier.getPaidTotal() - paymentDetailsDto.getPaymentAmount() - difference);
            supplier.setBalance((supplier.getTotalOrderValue() - supplier.getPaidTotal())+difference);
            supplier.setLastPayment(this.findLastPayment(supplier.getId()));
            supplierRepository.save(supplier);
        }

        if ("COMPLETED".equalsIgnoreCase(paymentDetailsDto.getPaymentStatus())) {
            supplier.setPaidTotal(supplier.getPaidTotal() + paymentDetailsDto.getPaymentAmount());
            supplier.setBalance(supplier.getTotalOrderValue() - supplier.getPaidTotal());
            supplier.setLastPayment(paymentDetailsDto.getPaymentAmount());
            supplierRepository.save(supplier);
        }
        // Map the PaymentDetailsDto to the existing PaymentDetails entity
        modelMapper.map(paymentDetailsDto, paymentDetails);
        paymentDetails.setSupplier(supplier);

        // Save and return the updated PaymentDetails
        return paymentDetailsRepository.save(paymentDetails); // Persist updated PaymentDetails
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer paymentDetailsId) throws Exception {
        try {
            if (!paymentDetailsRepository.existsById(paymentDetailsId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Payment details not found", 404));
            }
            PaymentDetails paymentDetails = paymentDetailsRepository.findById(paymentDetailsId).orElse(null);
            assert paymentDetails != null;
            if("COMPLETED".equalsIgnoreCase(paymentDetails.getPaymentStatus())){
                Supplier supplier = supplierRepository.findById(Math.toIntExact(paymentDetails.getSupplier().getId()))
                        .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + paymentDetails.getSupplier().getId()));
                supplier.setPaidTotal(supplier.getPaidTotal() - paymentDetails.getPaymentAmount());
                supplier.setBalance(supplier.getTotalOrderValue() - supplier.getPaidTotal());
                supplier.setLastPayment(this.findLastPayment(supplier.getId()));
                supplierRepository.save(supplier);
            }
            paymentDetailsRepository.deleteById(paymentDetailsId);
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Payment details: " + e.getMessage(), 500));
        }
    }





    public List<PaymentDetails> getLastThreePaymentsBySupplierId(Integer supplierId) {
        return paymentDetailsRepository.findTop3BySupplierIdOrderByPaymentDateDesc(supplierId);
    }

    public Double findLastPayment(Long supplierId) {
        PaymentDetails lastPaymentDetail = paymentDetailsRepository
                .findTopBySupplierIdAndPaymentStatusOrderByPaymentDateDesc(supplierId)
                .stream()
                .findFirst()
                .orElse(null);

        return lastPaymentDetail != null ? lastPaymentDetail.getPaymentAmount() : 0.0d;
    }


}
