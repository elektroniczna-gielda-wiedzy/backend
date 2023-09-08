package backend.controllers;

import backend.model.StandardResponse;
import backend.model.dto.CategoryDto;
import backend.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<StandardResponse> getCategoryList() {
        return categoryService.getCategoryList();
    }

    @PostMapping()
    public ResponseEntity<StandardResponse> addCategory(
            @RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<StandardResponse> editCategory(
            @PathVariable("category_id") Integer categoryId,
            @RequestBody CategoryDto categoryDto) {
        return categoryService.editCategory(categoryId, categoryDto);
    }

    @DeleteMapping("{category_id}")
    public ResponseEntity<StandardResponse> deleteCategory(
            @PathVariable("category_id") Integer categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}
