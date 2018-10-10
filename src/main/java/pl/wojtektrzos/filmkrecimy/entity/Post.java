package pl.wojtektrzos.filmkrecimy.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Movie movie;
    private String text;
    private LocalDateTime created;
    @ManyToOne
    private UserDetails author;

}
