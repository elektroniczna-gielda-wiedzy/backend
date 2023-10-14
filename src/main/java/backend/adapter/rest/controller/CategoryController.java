package backend.adapter.rest.controller;

import backend.adapter.rest.model.common.CategoryDto;
import backend.common.service.GenericServiceException;
import backend.entry.model.Category;
import backend.adapter.rest.Response;
import backend.adapter.rest.StandardBody;
import backend.entry.model.CategoryStatus;
import backend.entry.model.CategoryTranslationDto;
import backend.entry.service.CategoryService;
import backend.user.model.AppUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> getCategories(@AuthenticationPrincipal AppUserDetails userDetails,
                                                       @RequestParam Map<String, String> params) {
        String status = params.getOrDefault("status", "active").toUpperCase();
        boolean isAdmin = userDetails.getUser().getIsAdmin();
        if ((!status.equals(CategoryStatus.ACTIVE.name())) && !isAdmin) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage("You do not have permission to view suggestions or deleted categories")
                    .build();
        }
        Integer typeId = params.get("type") != null ? Integer.parseInt(params.get("type")) : null;
        Integer parentId = params.get("parent_id") != null ? Integer.parseInt(params.get("parent_id")) : null;
        List<Category> categories = this.categoryService.getCategories(status, typeId, parentId);

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(categories.stream().map(CategoryDto::buildFromModel).toList())
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> createCategory(@Valid @RequestBody CategoryDto categoryDto,
                                                       @AuthenticationPrincipal AppUserDetails userDetails) {
        Category category;
        boolean isSuggestion = categoryDto.getCategoryStatus().equals(CategoryStatus.SUGGESTED.name());
        boolean isAdmin = userDetails.getUser().getIsAdmin();
        if (!isSuggestion && !isAdmin) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage("You do not have permission to create a category, you can only suggest one")
                    .build();
        }

        try {
            category = this.categoryService.createCategory(
                    categoryDto.getCategoryStatus(),
                    categoryDto.getCategoryType(),
                    categoryDto.getNames().stream()
                                    .map(t -> new CategoryTranslationDto(t.getLanguageId(), t.getTranslatedName()))
                                    .toList(),
                    categoryDto.getParentId()
            );
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.CREATED)
                .result(List.of(CategoryDto.buildFromModel(category)))
                .build();
    }

    @PutMapping(path = "/{category_id}", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> updateCategory(@PathVariable("category_id") Integer categoryId,
                                                       @RequestBody CategoryDto categoryDto,
                                                       @AuthenticationPrincipal AppUserDetails userDetails) {
        Category category;

        if (!userDetails.getUser().getIsAdmin()) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage("You do not have permission to update the category")
                    .build();
        }

        try {
            category = this.categoryService.updateCategory(
                    categoryId,
                    categoryDto.getCategoryType(),
                    Optional.ofNullable(categoryDto.getNames())
                            .orElseGet(Collections::emptyList)
                            .stream()
                            .map(t -> new CategoryTranslationDto(t.getLanguageId(), t.getTranslatedName()))
                            .toList(),
                    categoryDto.getParentId(),
                    categoryDto.getCategoryStatus()
            );
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .result(List.of(CategoryDto.buildFromModel(category)))
                .build();
    }

    @DeleteMapping(path = "{category_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardBody> deleteCategory(@PathVariable("category_id") Integer categoryId,
                                                       @AuthenticationPrincipal AppUserDetails userDetails) {
        if (!userDetails.getUser().getIsAdmin()) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage("You do not have permission to delete the category")
                    .build();
        }

        try {
            this.categoryService.deleteCategory(categoryId);
        } catch (GenericServiceException exception) {
            return Response.builder()
                    .httpStatusCode(HttpStatus.BAD_REQUEST)
                    .addMessage(exception.getMessage())
                    .build();
        }

        return Response.builder()
                .httpStatusCode(HttpStatus.OK)
                .build();
    }
}
