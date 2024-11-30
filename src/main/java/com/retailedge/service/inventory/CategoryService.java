package com.retailedge.service.inventory;

import com.retailedge.entity.inventory.Category;
import com.retailedge.dto.inventory.*;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.inventory.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Category add(CategoryDto categoryDto){
        System.out.println("category ser "+ categoryDto.getCategory());
        Category category = modelMapper.map(categoryDto,Category.class);
        return categoryRepository.save(category);
    }

    public List<Category> list() {
        return  categoryRepository.findAll();
    }

    public Category update(Long categoryId, CategoryDto categoryDto) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        Category category =  categoryOptional.get();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
            return conditions.getSource() != null;
        });
        modelMapper.map(categoryDto,category);
        return categoryRepository.save(category);

    }


    public ResponseEntity<ResponseModel<?>> delete(Long categoryId) throws Exception {
        boolean hasProducts = productRepository.existsByCategoryId(Math.toIntExact(categoryId));

        if (hasProducts) {
            if (!categoryRepository.existsById(categoryId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Category not found", 404));
            }
            // Return 400 Bad Request if category has associated products
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel<>(false, "Cannot delete category with associated products.", 400));
        }

        try {
            categoryRepository.deleteById(categoryId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting category: " + e.getMessage(), 500));
        }
    }

}
