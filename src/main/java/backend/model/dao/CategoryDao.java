package backend.model.dao;

import backend.model.CategoryType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name = "Category")
@Getter
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

    @ManyToMany(mappedBy = "categories")
    private Set<EntryDao> entries;

    @OneToMany(mappedBy = "category")
    private Set<CategoryTranslation> categoryTranslations;
}
