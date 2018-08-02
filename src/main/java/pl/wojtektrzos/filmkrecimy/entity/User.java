package pl.wojtektrzos.filmkrecimy.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @Setter
    @Getter
    private UserDetails details;
    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    @NotBlank
    private String username;
    @Getter
    @Setter
    @NotBlank
    private String password;
    @Getter
    @Setter
    private boolean enabled;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Getter
    @Setter
    private Set<Authority> authorities;

}
