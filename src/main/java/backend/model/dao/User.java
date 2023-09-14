package backend.model.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_email_auth")
    private Boolean isEmailAuth;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(mappedBy = "likedBy")
    private Set<Entry> favorites;
}
