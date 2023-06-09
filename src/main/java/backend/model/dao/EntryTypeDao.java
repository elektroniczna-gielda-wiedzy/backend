package backend.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EntryType")
@Getter
@Setter
public class EntryTypeDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_type_id")
    private Integer entryTypeId;

    @Column(name = "name")
    private String name;
}
