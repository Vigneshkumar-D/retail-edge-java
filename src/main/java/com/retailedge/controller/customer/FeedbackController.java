//package com.retailedge.controller.customer;
//
//
//import com.retailedge.dto.customer.FeedbackDto;
//import com.retailedge.entity.customer.Feedback;
//import com.retailedge.service.customer.FeedbackService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/feedback")
//public class FeedbackController {
//
//    @Autowired
//    private FeedbackService feedbackService;
//
//    @GetMapping
//    public List<Feedback> list(){
//        return feedbackService.list();
//    }
//
//    @GetMapping("/{customerId}")
//    public List<Feedback> findByCustomer(@PathVariable("customerId") Integer customerId){
//        return feedbackService.findByCustomer(customerId);
//    }
//
//    @PostMapping
//    public Feedback add(@RequestBody FeedbackDto feedbackDto){
//        return feedbackService.add(feedbackDto);
//    }
//
//    @PutMapping("/{feedbackId}")
//    public Feedback update(@PathVariable("feedbackId") Integer feedbackId, @RequestBody FeedbackDto feedbackDto){
//        return feedbackService.update(feedbackId, feedbackDto);
//    }
//
//    @DeleteMapping("/{feedbackId}")
//    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("feedbackId") Integer feedbackId){
//       return feedbackService.delete(feedbackId);
//    }
//
//}
