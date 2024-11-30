package com.retailedge.service.store;

import com.retailedge.dto.store.StoreDetailsDto;
import com.retailedge.entity.store.StoreDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.store.StoreDetailsRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreDetailsService {
    @Autowired
    private StoreDetailsRepository storeDetailsRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<StoreDetails> list(){
        return storeDetailsRepository.findAll();
    }

    public StoreDetails add(StoreDetailsDto storeDetailsDto){
        return storeDetailsRepository.save(modelMapper.map(storeDetailsDto, StoreDetails.class));
    }

    public StoreDetails update(Integer storeDetailsId,  StoreDetailsDto storeDetailsDto) {
        StoreDetails  storeDetails = storeDetailsRepository.findById(storeDetailsId).orElse(null);
        if (storeDetails == null) {
            return null;
        }

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
            return conditions.getSource() != null;
        });
        modelMapper.map(storeDetailsDto, storeDetails);
        return storeDetailsRepository.save(storeDetails);
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer storeDetailsId) throws Exception {

        try {
            if (!storeDetailsRepository.existsById(storeDetailsId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Store details not found", 404));
            }
            storeDetailsRepository.deleteById(storeDetailsId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Store details: " + e.getMessage(), 500));
        }
    }

}
