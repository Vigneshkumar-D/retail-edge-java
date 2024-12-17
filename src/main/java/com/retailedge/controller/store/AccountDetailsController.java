package com.retailedge.controller.store;

import com.retailedge.dto.store.AccountDetailsDto;
import com.retailedge.entity.store.AccountDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.store.AccountDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("account-details")
public class AccountDetailsController {

    @Autowired
    public AccountDetailsService accountDetailsService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return accountDetailsService.list();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseModel<?>> add(@ModelAttribute AccountDetailsDto accountDetailsDto) throws IOException {
        return accountDetailsService.add(accountDetailsDto);
    }

    @PutMapping(value = "/{accountDetailsId}", consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("accountDetailsId") Integer accountDetailsId, @ModelAttribute AccountDetailsDto accountDetailsDto) throws IOException {
        return accountDetailsService.update(accountDetailsId, accountDetailsDto);
    }

    @DeleteMapping("/{accountDetailsId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("creditReminderId") Integer accountDetailsId) throws Exception {
       return accountDetailsService.delete(accountDetailsId);
    }
}
