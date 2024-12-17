package com.retailedge.service.emi;

import com.retailedge.entity.inventory.Product;
import com.retailedge.entity.customer.Customer;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.CustomerRepository;
import com.retailedge.dto.emi.EMIDetailsDto;
import com.retailedge.entity.emi.EMIDetails;
import com.retailedge.repository.emi.EMIDetailsRepository;
import com.retailedge.repository.inventory.ProductRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EMIDetailsService {

    @Autowired
    private EMIDetailsRepository emiDetailsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, emiDetailsRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving emi details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

//    public EMIDetails add(EMIDetailsDto emiDetailsDto){
////        EMIDetails emiDetails = modelMapper.map(emiDetailsDto, EMIDetails.class);
//        modelMapper.typeMap(EMIDetailsDto.class, EMIDetails.class).addMappings(mapper -> {
//            mapper.map(EMIDetailsDto::getProductId, (dest, value) -> dest.getProduct().setId((Long) value));
//            mapper.map(EMIDetailsDto::getCustomerId, (dest, value) -> dest.getCustomer().setId((Long) value));
//        });
//        Customer customer = customerRepository.findById(emiDetailsDto.getCustomerId()).get();
//        emiDetails.setCustomer(customer);
//        Product product = productRepository.findById(emiDetailsDto.getProductId()).get();
//        emiDetails.setProduct(product);
//        return emiDetailsRepository.save(emiDetails);
//    }

    public ResponseEntity<ResponseModel<?>> add(EMIDetailsDto emiDetailsDto) {
        try{
            EMIDetails emiDetails = new EMIDetails();
            emiDetails.setTotalAmount(emiDetailsDto.getTotalAmount());
            emiDetails.setEmiAmount(emiDetailsDto.getEmiAmount());
            emiDetails.setUpfront(emiDetailsDto.getUpfront());
            emiDetails.setScheme(emiDetailsDto.getScheme());
            emiDetails.setStartDate(emiDetailsDto.getStartDate());
            emiDetails.setEndDate(emiDetailsDto.getEndDate());
            emiDetails.setDescription(emiDetailsDto.getDescription());

            Optional<Customer> optionalCustomer = customerRepository.findById(emiDetailsDto.getCustomer().getId());
            if (optionalCustomer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Customer not found", 500));
            }
            Customer customer = optionalCustomer.get();
            emiDetails.setCustomer(customer);

            Optional<Product> optionalProduct = productRepository.findById(Math.toIntExact(emiDetailsDto.getProduct().getId()));
            if (optionalProduct.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Product not found", 500));
            }
            Product product = optionalProduct.get();
            emiDetails.setProduct(product);

            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, emiDetailsRepository.save(emiDetails)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding emi details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Integer emiDetailsId, EMIDetailsDto emiDetailsDto){
        try{
            Optional<EMIDetails> emiDetailsOptional = emiDetailsRepository.findById(emiDetailsId);
            if (emiDetailsOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "EMI Details not found", 500));
            }
            EMIDetails emiDetails = emiDetailsOptional.get();
            modelMapper.map(emiDetailsDto, emiDetails);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, emiDetailsRepository.save(emiDetails)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating emi details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }

    }

    public ResponseEntity<ResponseModel<?>> delete(Integer emiDetailsId) throws Exception {
        try {
            if (!emiDetailsRepository.existsById(emiDetailsId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "EMI Details not found", 404));
            }
            emiDetailsRepository.deleteById(emiDetailsId);
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting EMI Details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }
}
