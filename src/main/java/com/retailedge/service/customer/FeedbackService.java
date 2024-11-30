package com.retailedge.service.customer;

import com.retailedge.dto.customer.FeedbackDto;
import com.retailedge.entity.customer.Feedback;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.FeedbackRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Feedback> list(){
        return feedbackRepository.findAll();
    }

    public Feedback add(FeedbackDto feedbackDto){
        Feedback feedback = new Feedback();
        modelMapper.map(feedbackDto, feedback);
        return feedbackRepository.save(feedback);
    }

    public Feedback update(Integer feedbackId, FeedbackDto feedbackDto){
        Optional<Feedback> feedbackOptional = feedbackRepository.findById(feedbackId);

        if (feedbackOptional.isEmpty()) {
            throw new RuntimeException("Feedback Details not found with id: " + feedbackId);
        }

        Feedback feedback = feedbackOptional.get();

        modelMapper.map(feedback, feedbackDto);

        return feedbackRepository.save(feedback);
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer feedbackId) throws Exception {
        try {
            if (!feedbackRepository.existsById(feedbackId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Feedback not found", 404));
            }
            feedbackRepository.deleteById(feedbackId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Feedback: " + e.getMessage(), 500));
        }
    }

//    public List<Feedback> findByCustomer(Integer customerId) {
//        return feedbackRepository.findByCustomerId(customerId);
//    }
}

