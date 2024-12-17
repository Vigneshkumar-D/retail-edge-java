package com.retailedge.service.store;

import com.retailedge.dto.store.GSTDetailsDto;
import com.retailedge.entity.store.GSTDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.store.GSTDetailsRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GSTDetailsService {

    @Autowired
    private GSTDetailsRepository gstDetailsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, gstDetailsRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving gst details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }


    public ResponseEntity<ResponseModel<?>> add(GSTDetailsDto gstDetailsDto){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, gstDetailsRepository.save(modelMapper.map(gstDetailsDto, GSTDetails.class))));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding gst details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Integer gstDetailsId,  GSTDetailsDto gstDetailsDto) {

        try{
            GSTDetails  gstDetails = gstDetailsRepository.findById(gstDetailsId).orElse(null);
            if (gstDetails == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseModel<>(false, "GST details not found!", 400));
            }

            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.getConfiguration().setPropertyCondition(conditions -> {
                return conditions.getSource() != null;
            });
            modelMapper.map(gstDetailsDto, gstDetails);
            return ResponseEntity.ok(new ResponseModel<>(true, "Updated Successfully", 200, gstDetailsRepository.save(gstDetails)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating gst details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer gstDetailsId) throws Exception {

        try {
            if (!gstDetailsRepository.existsById(gstDetailsId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "GST details not found", 404));
            }
            gstDetailsRepository.deleteById(gstDetailsId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting GST details: " + e.getMessage(), 500));
        }
    }

}
