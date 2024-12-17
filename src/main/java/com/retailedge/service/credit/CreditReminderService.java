package com.retailedge.service.credit;

import com.retailedge.dto.credit.CreditReminderDto;
import com.retailedge.entity.credit.CreditReminder;
import com.retailedge.entity.customer.Customer;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.credit.CreditReminderRepository;
import com.retailedge.repository.customer.CustomerRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditReminderService {

    @Autowired
    private CreditReminderRepository creditReminderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success",200, creditReminderRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error fetching data: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> add(CreditReminderDto creditReminderDto){
        try{
            CreditReminder creditReminder = modelMapper.map(creditReminderDto, CreditReminder.class);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success",200, creditReminderRepository.save(creditReminder)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Authentication Error: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }


    }

    public ResponseEntity<ResponseModel<?>> update(Integer creditReminderId, CreditReminderDto creditReminderDto){
        try{
            Optional<CreditReminder> creditReminderOptional = creditReminderRepository.findById(creditReminderId);
            if (!creditReminderOptional.isPresent()) {
                throw new RuntimeException("Credit Reminder Details not found with id: " + creditReminderId);
            }
            CreditReminder creditReminder = creditReminderOptional.get();
            Optional<Customer> customer = customerRepository.findById(creditReminder.getCustomer().getId());
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.getConfiguration().setPropertyCondition(conditions -> {
                return conditions.getSource() != null;
            });
            modelMapper.map(creditReminderDto, creditReminder);
            creditReminder.setCustomer(customer.get());
            return ResponseEntity.ok(new ResponseModel<>(true, "Success",200, creditReminderRepository.save(creditReminder)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Authentication Error: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }


    }

    public ResponseEntity<ResponseModel<?>> delete(Integer creditReminderId) throws Exception {
        try {
            if (!creditReminderRepository.existsById(creditReminderId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Credit Reminder not found", 404));
            }
            creditReminderRepository.deleteById(creditReminderId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Credit Reminder: " + e.getMessage(), 500));
        }
    }

}
