package com.retailedge.service.store;

import com.retailedge.dto.store.AccountDetailsDto;
import com.retailedge.entity.store.AccountDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.store.AccountDetailsRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class AccountDetailsService {

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private ModelMapper modelMapper;


//    public AccountDetails add(AccountDetailsDto accountDetailsDto) throws IOException {
//        AccountDetails accountDetails = new AccountDetails();
//        modelMapper.map(accountDetailsDto, accountDetails);
//        if (accountDetailsDto.getUpiQRCode() != null && !accountDetailsDto.getUpiQRCode().isEmpty()) {
//            accountDetails.setUpiQRCode(accountDetailsDto.getUpiQRCode().getBytes());
//        }
//        return accountDetailsRepository.save(accountDetails);
//    }

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, accountDetailsRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving account details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> add(AccountDetailsDto accountDetailsDto) throws IOException {
        try{
            AccountDetails accountDetails = new AccountDetails();
            accountDetails.setBankName(accountDetailsDto.getBankName());
            accountDetails.setBranch(accountDetailsDto.getBranch());
            accountDetails.setAccountNumber(accountDetailsDto.getAccountNumber());
            accountDetails.setUpiId(accountDetailsDto.getUpiId());
            accountDetails.setIfscCode(accountDetailsDto.getIfscCode());
            if (accountDetailsDto.getUpiQRCodeImage() != null && !accountDetailsDto.getUpiQRCodeImage().isEmpty()) {
                accountDetails.setUpiQRCodeImage(accountDetailsDto.getUpiQRCodeImage().getBytes());
            }
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, accountDetailsRepository.save(accountDetails)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding account details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Integer accountDetailsId, AccountDetailsDto accountDetailsDto) throws IOException {
        try{
            AccountDetails  accountDetails = accountDetailsRepository.findById(accountDetailsId).orElse(null);
            if (accountDetails == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Account details not found!", 500));

            }
            accountDetails.setBankName(accountDetailsDto.getBankName());
            accountDetails.setBranch(accountDetailsDto.getBranch());
            accountDetails.setAccountNumber(accountDetailsDto.getAccountNumber());
            accountDetails.setUpiId(accountDetailsDto.getUpiId());
            accountDetails.setIfscCode(accountDetailsDto.getIfscCode());

            if (accountDetailsDto.getUpiQRCodeImage() != null && !accountDetailsDto.getUpiQRCodeImage().isEmpty()) {
                accountDetails.setUpiQRCodeImage(accountDetailsDto.getUpiQRCodeImage().getBytes());
            }
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, accountDetailsRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating account details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer accountDetailsId) throws Exception {
        try {
            if (!accountDetailsRepository.existsById(accountDetailsId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Account details not found!", 404));
            }
            accountDetailsRepository.deleteById(accountDetailsId);
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Account details: " + e.getMessage(), 500));
        }
    }

}
