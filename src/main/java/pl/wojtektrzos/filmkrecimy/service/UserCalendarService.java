package pl.wojtektrzos.filmkrecimy.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.entity.EventDate;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.repository.EventDateRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@NoArgsConstructor
public class UserCalendarService {
    @Autowired
    EventDateRepository eventDateRepository;

    public Map<LocalDate, Long> getUserCalendar(PlanItem owner) {
        Map<LocalDate, Long> outcome = new HashMap<>();
        for (EventDate eventDate : eventDateRepository.findAllByOwnerPlanItem(owner)){

        }
        return null;
    }

}
