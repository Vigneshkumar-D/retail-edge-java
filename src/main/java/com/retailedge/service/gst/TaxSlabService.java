package com.retailedge.service.gst;

import com.retailedge.entity.gst.TaxSlab;
import com.retailedge.dto.gst.TaxSlabDto;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.gst.TaxSlabRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TaxSlabService {

    @Autowired
    private TaxSlabRepository taxSlabRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;


    public BigDecimal calculateGST(String category, String region, BigDecimal price, boolean isInterstate) {
        TaxSlab taxSlab = taxSlabRepository.findByCategory_CategoryAndRegion(category, region);

        if (isInterstate) {
            BigDecimal igst = taxSlab.getIgst().multiply(price).divide(BigDecimal.valueOf(100));
            return igst;
        } else {
            BigDecimal sgst = taxSlab.getSgst().multiply(price).divide(BigDecimal.valueOf(100));
            BigDecimal cgst = taxSlab.getCgst().multiply(price).divide(BigDecimal.valueOf(100));
            return sgst.add(cgst);
        }
    }

    public ResponseEntity<ResponseModel<?>> list() {
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, taxSlabRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving tax slab details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> add(TaxSlabDto taxSlabDto) {
        try{
            TaxSlab taxSlab = modelMapper.map(taxSlabDto, TaxSlab.class);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, taxSlabRepository.save(taxSlab)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding tax slab details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Long taxSlabId, TaxSlabDto taxSlabDto) {
        try{
            Optional<TaxSlab> taxSlabOptional = taxSlabRepository.findById(taxSlabId);
            if (taxSlabOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Tax Slab not found!", 500));
            }
            TaxSlab taxSlab = taxSlabOptional.get();
            modelMapper.map(taxSlabDto, taxSlab);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, taxSlabRepository.save(taxSlab)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding tax slab details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> delete(Long taxSlabId) throws Exception {
        try {
            if (!taxSlabRepository.existsById(taxSlabId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Tax Slab not found", 404));
            }
            taxSlabRepository.deleteById(Long.valueOf(taxSlabId));
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Tax Slab: " + e.getMessage(), 500));
        }
    }
}
