package backend.model.dao;

import backend.model.CategoryType;
import jakarta.persistence.*;

@Entity
@Table(name = "Category")
public class CategoryDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "type")
    private CategoryType categoryType;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CategoryDao parentCategory;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
