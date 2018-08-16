package pl.wojtektrzos.filmkrecimy.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.entity.EventDate;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.repository.EventDateRepository;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserCalendarService {
    @Autowired
    EventDateRepository eventDateRepository;

    public Map<LocalDate, PlanItem> getUserCalendarWithOcupant(PlanItem owner) {
        Map<LocalDate, PlanItem> outcome = new HashMap<>();
        for (EventDate eventDate : eventDateRepository.findAllByOwnerPlanItem(owner)){
            outcome.put(eventDate.getDate(),eventDate.getOccupiedBy());
        }
        return outcome;
    }

    public List<LocalDate> getYear(){
        List<LocalDate> outcome = new ArrayList<>();
        for(int i = 0; i<365;i++)
        {
            outcome.add(LocalDate.now().plusDays(i));
        }
        return outcome;
    }

}
