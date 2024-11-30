package com.retailedge.service.store;

import com.retailedge.dto.store.AccountDetailsDto;
import com.retailedge.entity.store.AccountDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.store.AccountDetailsRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AccountDetailsService {

    @Autowired
    private AccountDetailsRepository accountDetailsRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<AccountDetails> list(){
        return accountDetailsRepository.findAll();
    }

    public AccountDetails add(AccountDetailsDto accountDetailsDto) throws IOException {
        AccountDetails accountDetails = modelMapper.map(accountDetailsDto, AccountDetails.class);
        byte[] qrCodeBytes = accountDetailsDto.getUpiQRCode().getBytes();
        accountDetails.setUpiQRCode(qrCodeBytes);
        return accountDetailsRepository.save(accountDetails);
    }

    public AccountDetails update(Integer accountDetailsId, AccountDetailsDto accountDetailsDto) {
        AccountDetails  accountDetails = accountDetailsRepository.findById(accountDetailsId).orElse(null);
        if (accountDetails == null) {
            return null;
        }

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
            return conditions.getSource() != null;
        });
        modelMapper.map(accountDetailsDto, accountDetails);
        return accountDetailsRepository.save(accountDetails);
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer accountDetailsId) throws Exception {

        try {
            if (!accountDetailsRepository.existsById(accountDetailsId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Account details not found", 404));
            }
            accountDetailsRepository.deleteById(accountDetailsId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Account details: " + e.getMessage(), 500));
        }
    }

}
