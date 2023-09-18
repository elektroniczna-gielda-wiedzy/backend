package backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EntryType")
@Getter
@Setter
public class EntryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_type_id")
    private Integer id;

    @Column(name = "name")
    private String name;
}
