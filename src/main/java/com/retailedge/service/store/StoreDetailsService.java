package com.retailedge.service.store;

import com.retailedge.dto.store.StoreDetailsDto;
import com.retailedge.entity.inventory.Product;
import com.retailedge.entity.store.StoreDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.store.StoreDetailsRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class StoreDetailsService {
    @Autowired
    private StoreDetailsRepository storeDetailsRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, storeDetailsRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving store details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> add(StoreDetailsDto storeDetailsDto) throws IOException {
        try{
            StoreDetails storeDetails = new StoreDetails();
            storeDetails.setStoreName(storeDetailsDto.getStoreName());
            storeDetails.setAddress(storeDetailsDto.getAddress());
            storeDetails.setState(storeDetailsDto.getState());
            storeDetails.setPinCode(storeDetailsDto.getPinCode());
            storeDetails.setPrimaryPhone(storeDetailsDto.getPrimaryPhone());
            storeDetails.setSecondaryPhone(storeDetailsDto.getSecondaryPhone());
            if (storeDetailsDto.getStoreLogoImage() != null && !storeDetailsDto.getStoreLogoImage().isEmpty()) {
                storeDetails.setStoreLogoImage(storeDetailsDto.getStoreLogoImage().getBytes());
            }
            return ResponseEntity.ok(new ResponseModel<>(true, "Added Successfully", 200, storeDetailsRepository.save(storeDetails)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding store details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Integer storeDetailsId, StoreDetailsDto storeDetailsDto) throws IOException {
        try{
            StoreDetails  storeDetails = storeDetailsRepository.findById(storeDetailsId).orElse(null);
            if (storeDetails == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Store details not found!", 500));
            }
            storeDetails.setStoreName(storeDetailsDto.getStoreName());
            storeDetails.setAddress(storeDetailsDto.getAddress());
            storeDetails.setState(storeDetailsDto.getState());
            storeDetails.setPinCode(storeDetailsDto.getPinCode());
            storeDetails.setPrimaryPhone(storeDetailsDto.getPrimaryPhone());
            storeDetails.setSecondaryPhone(storeDetailsDto.getSecondaryPhone());
            if (storeDetailsDto.getStoreLogoImage() != null && !storeDetailsDto.getStoreLogoImage().isEmpty()) {
                storeDetails.setStoreLogoImage(storeDetailsDto.getStoreLogoImage().getBytes());
            }
                        return ResponseEntity.ok(new ResponseModel<>(true, "Updated Successfully", 200, storeDetailsRepository.save(storeDetails)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating store details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
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
