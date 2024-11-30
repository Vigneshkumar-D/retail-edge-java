package com.retailedge.service.inventory;

import com.retailedge.dto.inventory.StockReportHistoryDto;
import com.retailedge.entity.inventory.StockReportHistory;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.inventory.StockReportHistoryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockReportHistoryService {

    @Autowired
    private StockReportHistoryRepository stockReportHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<StockReportHistory> list(){
        return stockReportHistoryRepository.findAll();
    }

    public StockReportHistory add(StockReportHistoryDto stockReportHistoryDto) {
        return stockReportHistoryRepository.save(modelMapper.map(stockReportHistoryDto, StockReportHistory.class));
    }

    public StockReportHistory update(StockReportHistoryDto stockReportHistoryDto, Integer stockReportHistoryId) {

        Optional<StockReportHistory> stockReportHistory = stockReportHistoryRepository.findById(Long.valueOf(stockReportHistoryId));
        if(stockReportHistory.isPresent()){
            StockReportHistory stockReportHistory1 =  stockReportHistory.get();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.getConfiguration().setPropertyCondition(conditions -> {
                return conditions.getSource() != null;
            });
            modelMapper.map(stockReportHistoryDto,stockReportHistory1);
            return stockReportHistoryRepository.save(stockReportHistory1);
        }
        return null;
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer stockReportHistoryId) throws Exception {
        try {
            if (!stockReportHistoryRepository.existsById(Long.valueOf(stockReportHistoryId))) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Stock Report History not found", 404));
            }
            stockReportHistoryRepository.deleteById(Long.valueOf(stockReportHistoryId));
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Stock Report History: " + e.getMessage(), 500));
        }
    }

}
