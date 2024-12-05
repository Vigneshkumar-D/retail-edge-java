package com.retailedge.service.inventory;

import com.retailedge.dto.inventory.LowStockAlertDto;
import com.retailedge.entity.inventory.LowStockAlert;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.inventory.LowStockAlertRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LowStockAlertService {

    @Autowired
    private LowStockAlertRepository lowStockAlertRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<LowStockAlert> list() {
        return lowStockAlertRepository.findAll();
    }

    public LowStockAlert add(LowStockAlertDto lowStockAlertDto) {
        LowStockAlert lowStockAlert = new LowStockAlert();
        modelMapper.map(lowStockAlertDto, lowStockAlert);
        return lowStockAlertRepository.save(lowStockAlert);
    }

    public LowStockAlert update(Integer lowStockAlertId, LowStockAlertDto lowStockAlertDto) {

        Optional<LowStockAlert> lowStockAlertOptional = lowStockAlertRepository.findById(lowStockAlertId);

        if (!lowStockAlertOptional.isPresent()) {
            throw new RuntimeException("Product not found with id: " + lowStockAlertId);
        }

        LowStockAlert lowStockAlert = lowStockAlertOptional.get();

        modelMapper.map(lowStockAlertDto, lowStockAlert);

        return lowStockAlertRepository.save(lowStockAlert);
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer lowStockAlertId) throws Exception {
        try {
            if (!lowStockAlertRepository.existsById(lowStockAlertId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Low stock alert not found", 404));
            }
            lowStockAlertRepository.deleteById(lowStockAlertId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Low stock alert: " + e.getMessage(), 500));
        }
    }

}
