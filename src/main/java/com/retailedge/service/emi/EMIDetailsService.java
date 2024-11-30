package com.retailedge.service.emi;

import com.retailedge.entity.inventory.Product;
import com.retailedge.entity.customer.Customer;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.CustomerRepository;
import com.retailedge.dto.emi.EMIDetailsDto;
import com.retailedge.entity.emi.EMIDetails;
import com.retailedge.repository.emi.EMIDetailsRepository;
import com.retailedge.repository.inventory.ProductRepository;
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

    public List<EMIDetails> list(){
        return emiDetailsRepository.findAll();
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

    public EMIDetails add(EMIDetailsDto emiDetailsDto) {
        EMIDetails emiDetails = new EMIDetails();

        emiDetails.setTotalAmount(emiDetailsDto.getTotalAmount());
        emiDetails.setEmiAmount(emiDetailsDto.getEmiAmount());
        emiDetails.setUpfront(emiDetailsDto.getUpfront());
        emiDetails.setScheme(emiDetailsDto.getScheme());
        emiDetails.setStartDate(emiDetailsDto.getStartDate());
        emiDetails.setEndDate(emiDetailsDto.getEndDate());
        emiDetails.setDescription(emiDetailsDto.getDescription());

        // Set the customer manually
        Customer customer = customerRepository.findById(emiDetailsDto.getCustomer().getId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        emiDetails.setCustomer(customer);

        // Set the product manually
        Product product = productRepository.findById(Math.toIntExact(emiDetailsDto.getProduct().getId()))
                .orElseThrow(() -> new RuntimeException("Product not found"));
        emiDetails.setProduct(product);

        // Save the entity
        return emiDetailsRepository.save(emiDetails);
    }



    public EMIDetails update(Integer emiDetailsId, EMIDetailsDto emiDetailsDto){
        Optional<EMIDetails> emiDetailsOptional = emiDetailsRepository.findById(emiDetailsId);
        if (emiDetailsOptional.isEmpty()) {
            throw new RuntimeException("EMI Details not found with id: " + emiDetailsId);
        }
        EMIDetails emiDetails = emiDetailsOptional.get();
        modelMapper.map(emiDetailsDto, emiDetails);
        return emiDetailsRepository.save(emiDetails);
    }


    public ResponseEntity<ResponseModel<?>> delete(Integer emiDetailsId) throws Exception {
        try {
            if (!emiDetailsRepository.existsById(emiDetailsId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "EMI Details not found", 404));
            }
            emiDetailsRepository.deleteById(emiDetailsId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting EMI Details: " + e.getMessage(), 500));
        }
    }

}
