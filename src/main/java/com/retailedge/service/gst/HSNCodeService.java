package com.retailedge.service.gst;

import com.retailedge.entity.gst.HSNCode;
import com.retailedge.dto.gst.HSNCodeDto;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.gst.HSNCodeRepository;
import com.retailedge.repository.inventory.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HSNCodeService {

    @Autowired
    private HSNCodeRepository hsnCodeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    public HSNCode getHSNCode(String code) {
        return hsnCodeRepository.findByCode(code);
    }

    public List<HSNCode> list() {
        return hsnCodeRepository.findAll();
    }

    public HSNCode add(HSNCodeDto hsnCodeDto){
        HSNCode hsnCode = new HSNCode();
        modelMapper.map(hsnCodeDto, hsnCode);
//        Optional<Category> category = categoryRepository.findById(hsnCodeDto.getTaxSlab().getCategory().getId());
//        hsnCode.getTaxSlab().setCategory(category.get());
        return hsnCodeRepository.save(hsnCode);
    }

    public HSNCode update(Long hsnCodeId, HSNCodeDto hsnCodeDto) {
        Optional<HSNCode> hsnCodeOptional = hsnCodeRepository.findById(hsnCodeId);

        if (!hsnCodeOptional.isPresent()) {
            throw new RuntimeException("HSN code not found with id: " + hsnCodeId);
        }

        HSNCode hsnCode = hsnCodeOptional.get();


        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
            return conditions.getSource() != null;
        });

        modelMapper.map(hsnCodeDto, hsnCode);

        return hsnCodeRepository.save(hsnCode);
    }


    public ResponseEntity<ResponseModel<?>> delete(Long hsnCodeId) throws Exception {
        try {
            if (!hsnCodeRepository.existsById(hsnCodeId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "HSN Code not found", 404));
            }
            hsnCodeRepository.deleteById(hsnCodeId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting HSN Code: " + e.getMessage(), 500));
        }
    }

}
