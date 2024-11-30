package com.retailedge.service.store;

import com.retailedge.dto.store.GSTDetailsDto;
import com.retailedge.entity.store.GSTDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.store.GSTDetailsRepository;
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

    public List<GSTDetails> list(){
        return gstDetailsRepository.findAll();
    }

    public GSTDetails add(GSTDetailsDto gstDetailsDto){
        return gstDetailsRepository.save(modelMapper.map(gstDetailsDto, GSTDetails.class));
    }

    public GSTDetails update(Integer gstDetailsId,  GSTDetailsDto gstDetailsDto) {
        GSTDetails  gstDetails = gstDetailsRepository.findById(gstDetailsId).orElse(null);
        if (gstDetails == null) {
            return null;
        }

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
            return conditions.getSource() != null;
        });
        modelMapper.map(gstDetailsDto, gstDetails);
        return gstDetailsRepository.save(gstDetails);
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
