package com.retailedge.controller.credit;

import com.retailedge.dto.credit.CreditReminderDto;
import com.retailedge.entity.credit.CreditReminder;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.credit.CreditReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credit-reminder")
public class CreditReminderController {

    @Autowired
    public CreditReminderService creditReminderService;

    @GetMapping
    public List<CreditReminder> list(){
        return creditReminderService.list();
    }

    @PostMapping
    public CreditReminder add(@RequestBody CreditReminderDto creditReminderDto){
        return creditReminderService.add(creditReminderDto);
    }

    @PutMapping("/{creditReminderId}")
    public CreditReminder update(@PathVariable("creditReminderId") Integer creditReminderId, @RequestBody CreditReminderDto creditReminderDto){
        return creditReminderService.update(creditReminderId, creditReminderDto);
    }

    @DeleteMapping("/{creditReminderId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("creditReminderId") Integer creditReminderId) throws Exception {
        return creditReminderService.delete(creditReminderId);
    }
}
