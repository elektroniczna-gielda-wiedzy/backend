package backend.entry.service;

import backend.entry.model.Category;
import backend.entry.repository.CategoryRepository;
import backend.adapter.rest.model.common.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getCategories() {
        return this.categoryRepository.findAll();
    }

    public Category addCategory(CategoryDto categoryDto) {
        // TODO
        return null;
    }

    public Category editCategory(Integer categoryId, CategoryDto categoryDto) {
        return null;
    }

    public void deleteCategory(Integer categoryId) {
    }
}