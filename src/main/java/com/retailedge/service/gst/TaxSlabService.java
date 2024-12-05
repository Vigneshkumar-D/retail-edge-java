package com.retailedge.service.gst;

import com.retailedge.entity.gst.TaxSlab;
import com.retailedge.dto.gst.TaxSlabDto;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.gst.TaxSlabRepository;
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

    public TaxSlab add(TaxSlabDto taxSlabDto) {
        TaxSlab taxSlab = modelMapper.map(taxSlabDto, TaxSlab.class);
        System.out.println("tax "+taxSlab.getRegion());
        return taxSlabRepository.save(taxSlab);
    }

    public List<TaxSlab> list() {
        return taxSlabRepository.findAll();
    }

    public TaxSlab update(Long taxSlabId, TaxSlabDto taxSlabDto) {
        Optional<TaxSlab> taxSlabOptional = taxSlabRepository.findById(taxSlabId);

        if (!taxSlabOptional.isPresent()) {
            throw new RuntimeException("Tax Slab not found with id: " + taxSlabId);
        }

        TaxSlab taxSlab = taxSlabOptional.get();

        modelMapper.map(taxSlabDto, taxSlab);

        return taxSlabRepository.save(taxSlab);
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
