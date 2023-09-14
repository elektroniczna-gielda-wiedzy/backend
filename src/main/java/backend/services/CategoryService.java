package backend.services;

import backend.model.dao.Category;
import backend.model.dto.CategoryDto;
import backend.repositories.CategoryRepository;
import backend.rest.common.StandardBody;
import backend.util.CategoriesDaoDtoConverter;
import backend.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<StandardBody> getCategoryList() {
        try {
            List<Category> categories = categoryRepository.findAll();
            List<CategoryDto> categoryDtoList = categories.stream()
                    .map(CategoriesDaoDtoConverter::convertToDto)
                    .collect(Collectors.toList());

            StandardBody response = StandardBody.builder()
                    .success(true)
                    .messages(List.of())
                    .result(categoryDtoList)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardBody.builder()
                                  .success(false)
                                  .messages(List.of(e.getMessage()))
                                  .result(List.of())
                                  .build());
        }
    }

    public ResponseEntity<StandardBody> addCategory(CategoryDto categoryDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardBody> editCategory(Integer categoryId, CategoryDto categoryDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardBody> deleteCategory(Integer categoryId) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
