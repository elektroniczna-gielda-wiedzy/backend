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
    private CategoryType type;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Column(name = "status")
    private CategoryStatus status;

    @ManyToMany(mappedBy = "categories")
    private Set<Entry> entries;

    @OneToMany(mappedBy = "category")
    private Set<CategoryTranslation> translations;

    public static Specification<Category> hasStatus(Integer status) {
        if (status == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Category> hasType(Integer type) {
        if (type == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type);
    }

    public static Specification<Category> hasParent(Integer parentId) {
        if (parentId == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        if (parentId == -1) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("parent"));
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("parent").get("id"), parentId);
    }

    public static Specification<Category> hasParentActive() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("parent").get("status"),
                                                                       CategoryStatus.ACTIVE);
    }
}
