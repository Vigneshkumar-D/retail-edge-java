package com.retailedge.service.inventory;

import com.retailedge.entity.inventory.Product;
import com.retailedge.dto.inventory.StockTransactionDto;
import com.retailedge.entity.inventory.StockTransaction;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.inventory.ProductRepository;
import com.retailedge.repository.inventory.StockTransactionRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockTransactionService {

    @Autowired
    private StockTransactionRepository stockTransactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, stockTransactionRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving stock transaction details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> add(StockTransactionDto stockTransactionDto){

        try{
            StockTransaction stockTransaction = new StockTransaction();
            Product product = productRepository.findById(stockTransactionDto.getProduct().getId()).get();
            modelMapper.map(stockTransactionDto, stockTransaction);
            stockTransaction.setProduct(product);
            product.setStockLevel(product.getStockLevel() - stockTransactionDto.getQuantity());
            productRepository.save(product);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, stockTransactionRepository.save(stockTransaction)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding stock transaction details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Integer stockTransactionId,  StockTransactionDto stockTransactionDto){
        try{
            Optional<StockTransaction> stockTransactionOptional = stockTransactionRepository.findById(stockTransactionId);
            if (stockTransactionOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Stock transaction details not found!", 500));
            }
            StockTransaction stockTransaction = stockTransactionOptional.get();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.getConfiguration().setPropertyCondition(conditions -> {
                return conditions.getSource() != null;
            });
            modelMapper.map(stockTransactionDto, stockTransaction);

            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, stockTransactionRepository.save(stockTransaction)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating stock transaction details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer stockTransactionId) throws Exception {
        try {
            if (!stockTransactionRepository.existsById(stockTransactionId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Stock transaction not found", 404));
            }
            stockTransactionRepository.deleteById(stockTransactionId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Stock transaction: " + e.getMessage(), 500));
        }
    }

}
