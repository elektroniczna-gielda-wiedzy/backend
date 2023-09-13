package backend.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CategoryTranslation")
@Getter
@Setter
public class CategoryTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_translation_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryDao category;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private LanguageDao language;

    @Column(name = "translation")
    private String translation;
}
