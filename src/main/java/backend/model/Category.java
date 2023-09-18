package backend.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name = "Category")
@Getter
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

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToMany(mappedBy = "categories")
    private Set<Entry> entries;

    @OneToMany(mappedBy = "category")
    private Set<CategoryTranslation> categoryTranslations;
}
