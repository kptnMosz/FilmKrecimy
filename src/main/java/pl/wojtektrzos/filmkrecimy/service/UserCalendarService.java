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

    public Map<LocalDate, EventDate> getEventDatesMapForPlaiItem(PlanItem owner) {
        Map<LocalDate, EventDate> eventsWithDate = new HashMap<>();
        List<EventDate> ownerEvents = eventDateRepository.findAllByOwnerPlanItem(owner);
        for(EventDate eventDate:ownerEvents){
            eventsWithDate.put(eventDate.getDate(), eventDate);
        }
        return eventsWithDate;
    }

    public List<LocalDate> getYear() {
        List<LocalDate> outcome = new ArrayList<>();
        for (int i = 0; i < 365; i++) {
            outcome.add(LocalDate.now().plusDays(i));
        }
        return outcome;
    }

}
