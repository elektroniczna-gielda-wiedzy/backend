package backend.services;

import backend.model.StandardResponse;
import backend.model.dao.CategoryDao;
import backend.model.dto.CategoryDto;
import backend.repositories.CategoryRepository;
import backend.util.ResponseUtil;
import backend.util.CategoriesDaoDtoConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<StandardResponse> getCategoryList() {
        try {
            List<CategoryDao> categories = categoryRepository.findAll();
            List<CategoryDto> categoryDtoList = categories.stream()
                    .map(CategoriesDaoDtoConverter::convertToDto)
                    .collect(Collectors.toList());

            StandardResponse response = StandardResponse.builder()
                    .success(true)
                    .messages(List.of())
                    .result(categoryDtoList)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(StandardResponse.builder()
                            .success(false)
                            .messages(List.of(e.getMessage()))
                            .result(List.of())
                            .build());
        }
    }

    public ResponseEntity<StandardResponse> addCategory(
            CategoryDto categoryDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> editCategory(
            Integer categoryId,
            CategoryDto categoryDto) {
        return ResponseUtil.getNotImplementedResponse();
    }

    public ResponseEntity<StandardResponse> deleteCategory(
            Integer categoryId) {
        return ResponseUtil.getNotImplementedResponse();
    }
}
