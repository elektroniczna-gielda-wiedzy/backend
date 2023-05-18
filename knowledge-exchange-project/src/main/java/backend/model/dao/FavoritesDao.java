package backend.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Favorites")
@Getter
@Setter
public class FavoritesDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Integer favoriteId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao user;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private EntryDao entry;

}
