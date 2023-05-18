package backend.services;

import backend.model.StandardResponse;
import backend.model.dto.CategoryDto;
import backend.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    public ResponseEntity<StandardResponse> getCategoryList() {
        return ResponseUtil.getNotImplementedResponse();
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
