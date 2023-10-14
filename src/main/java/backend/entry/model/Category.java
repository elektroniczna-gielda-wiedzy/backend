package backend.entry.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@Entity
@Table(name = "Category")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    @Column(name = "type")
    private CategoryType categoryType;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @Column(name = "status")
    private CategoryStatus categoryStatus;

    @ManyToMany(mappedBy = "categories")
    private Set<Entry> entries;

    @OneToMany(mappedBy = "category")
    private Set<CategoryTranslation> categoryTranslations;

    public static Specification<Category> hasStatus(CategoryStatus status) {
        if (status == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("categoryStatus"), status);
    }

    public static Specification<Category> hasType(CategoryType type) {
        if (type == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("categoryType"), type);
    }

    public static Specification<Category> hasParent(Integer parentId) {
        if (parentId == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        if (parentId == -1) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("parentCategory"));
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("parentCategory").get("id"), parentId);
    }

}
