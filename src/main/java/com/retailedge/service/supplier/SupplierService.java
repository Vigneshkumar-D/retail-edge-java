package com.retailedge.service.supplier;

import com.google.zxing.qrcode.decoder.Mode;
import com.retailedge.dto.supplier.SupplierDto;
import com.retailedge.entity.suppiler.Supplier;
import com.retailedge.entity.user.Role;
import com.retailedge.entity.user.User;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.supplier.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Supplier> list(Specification<Supplier> spec){
        return supplierRepository.findAll(spec);
    }

    public Supplier add(SupplierDto supplierDto){
        Supplier supplier = modelMapper.map(supplierDto, Supplier.class);
        supplier.setLastPayment(0.0);
        supplier.setBalance(0.0);
        supplier.setPaidTotal(0.0);
        supplier.setTotalOrderValue(0.0);
        supplier.setPaidTotal(0.0);
        return supplierRepository.save(supplier);
    }

    public Supplier update(Integer supplierId, SupplierDto supplierDto){
        Supplier supplier = supplierRepository.findById(supplierId).orElse(null);
        if (supplier == null) {
            throw new RuntimeException("Supplier Details not found with id: " + supplierId);
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
            return conditions.getSource() != null;
        });

        modelMapper.map(supplierDto, supplier);
        return supplierRepository.save(supplier);
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer supplierId) throws Exception {

        try {
            if (!supplierRepository.existsById(supplierId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Supplier not found", 404));
            }
            supplierRepository.deleteById(supplierId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting supplier: " + e.getMessage(), 500));
        }
    }

}
