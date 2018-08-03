package pl.wojtektrzos.filmkrecimy.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import pl.wojtektrzos.filmkrecimy.entity.Activity;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import pl.wojtektrzos.filmkrecimy.entity.PlanItemRole;

@NoArgsConstructor
@ToString
@Getter
@Setter
@Component
public class ActivityDto {
    private Activity activity;
    private PlanItemRole planItemRole;
    private int numberOfDays;
    private Movie movie;

}
