package pl.wojtektrzos.filmkrecimy.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;
import java.util.Set;

@Entity
@Repository
@Getter
@Setter
@NoArgsConstructor
public class UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private User logInfo;
    @OneToOne
    private PlanItem planMyself;
    @OneToMany(mappedBy = "owner")
    private List<PlanItem> myEvents;
    @OneToMany(mappedBy = "owner")
    private List<PlanItem> myAssets;
    @Email
    private String email;

}
