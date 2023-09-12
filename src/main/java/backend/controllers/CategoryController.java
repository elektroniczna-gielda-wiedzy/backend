package backend.controllers;

import backend.model.dto.CategoryDto;
import backend.rest.common.StandardBody;
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
    public ResponseEntity<StandardBody> getCategoryList() {
        return categoryService.getCategoryList();
    }

    @PostMapping()
    public ResponseEntity<StandardBody> addCategory(
            @RequestBody CategoryDto categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<StandardBody> editCategory(
            @PathVariable("category_id") Integer categoryId,
            @RequestBody CategoryDto categoryDto) {
        return categoryService.editCategory(categoryId, categoryDto);
    }

    @DeleteMapping("{category_id}")
    public ResponseEntity<StandardBody> deleteCategory(
            @PathVariable("category_id") Integer categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}
