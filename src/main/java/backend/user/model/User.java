package backend.user.model;

import backend.entry.model.Entry;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.List;
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

    @Column(name = "is_banned")
    private Boolean isBanned;

    @ManyToMany(mappedBy = "likedBy")
    private Set<Entry> favorites;

    @OneToMany(mappedBy = "author")
    private List<Entry> entries;

    public static Specification<User> hasIsBanned(Boolean isBanned) {
        if (isBanned == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isBanned"), isBanned);
    }

    public static Specification<User> hasIsEmailAuth(Boolean isEmailAuth) {
        if (isEmailAuth == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isEmailAuth"), isEmailAuth);
    }

    public static Specification<User> matchesQuery(String query) {
        if (query == null) {
            return (root, query1, criteriaBuilder) -> criteriaBuilder.conjunction();
        }
        return (root, query1, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + query.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + query.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(
                                             criteriaBuilder.concat(
                                                     criteriaBuilder.concat(root.get("firstName"), " "),
                                                     root.get("lastName"))),
                                     "%" + query.toLowerCase() + "%"),

                criteriaBuilder.like(criteriaBuilder.lower(
                                             criteriaBuilder.concat(
                                                     criteriaBuilder.concat(root.get("lastName"), " "),
                                                     root.get("firstName"))),
                                     "%" + query.toLowerCase() + "%")
        );
    }
}
