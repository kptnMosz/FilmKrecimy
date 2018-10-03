package pl.wojtektrzos.filmkrecimy.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String name;
    private String description;
    @ManyToMany(mappedBy = "movie")
    private Set<PlanItem> observers;
    @ManyToOne
    private UserDetails owner;
    @OneToMany(mappedBy = "movie")
    private Set<Activity> activities;
    @OneToMany(mappedBy = "movie")
    private List<Post> posts;
}
