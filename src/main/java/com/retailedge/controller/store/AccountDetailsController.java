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
    public List<AccountDetails> list(){
        return accountDetailsService.list();
    }

//    @PostMapping
//    public AccountDetails add(@RequestBody AccountDetailsDto accountDetailsDto){
//        return accountDetailsService.add(accountDetailsDto);
//    }

    @PostMapping(consumes = {"multipart/form-data"})
    public AccountDetails add(@ModelAttribute AccountDetailsDto accountDetailsDto) throws IOException {
        // Process the uploaded file and other data
        return accountDetailsService.add(accountDetailsDto);
    }

    @PutMapping("/{accountDetailsId}")
    public AccountDetails update(@PathVariable("accountDetailsId") Integer accountDetailsId, @RequestBody AccountDetailsDto accountDetailsDto){
        return accountDetailsService.update(accountDetailsId, accountDetailsDto);
    }

    @DeleteMapping("/{accountDetailsId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("creditReminderId") Integer accountDetailsId) throws Exception {
       return accountDetailsService.delete(accountDetailsId);
    }
}
