package backend.rest.category;

import backend.model.dao.Category;
import backend.rest.common.Response;
import backend.rest.common.StandardBody;
import backend.rest.common.model.CategoryDto;
import backend.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<StandardBody> getCategoryList() {
        List<Category> categories = this.categoryService.getCategories();

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(categories.stream().map(CategoryDto::buildFromModel).toList())
                .build();
    }

    @PostMapping()
    public ResponseEntity<StandardBody> addCategory(@RequestBody CategoryDto categoryDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<StandardBody> editCategory(@PathVariable("category_id") Integer categoryId,
                                                     @RequestBody CategoryDto categoryDto) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }

    @DeleteMapping("{category_id}")
    public ResponseEntity<StandardBody> deleteCategory(@PathVariable("category_id") Integer categoryId) {
        return Response.builder()
                .httpStatusCode(HttpStatus.NOT_IMPLEMENTED)
                .build();
    }
}
