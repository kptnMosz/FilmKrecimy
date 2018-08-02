package pl.wojtektrzos.filmkrecimy.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.entity.EventDate;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.entity.Prerequisite;
import pl.wojtektrzos.filmkrecimy.repository.EventDateRepository;
import pl.wojtektrzos.filmkrecimy.util.EnterLog;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Service
public class EventUpdaterService {
    @Autowired
    EventDateRepository eventDateRepository;

    public void updateEvent(PlanItem event, LocalDate date, Map<Prerequisite, PlanItem> mapOutcomeForDate) {
        EnterLog.log("UpdaterService", "wchodze", mapOutcomeForDate.toString());
        if (event.getAvailibleAfter() != null) {
            List<EventDate> eventDates = eventDateRepository.findAllByOccupiedBy(event);
            EnterLog.log("updateEvent---===26===---", "zrzucam liste juz nieaktualnych zasobow", eventDates.toString());
            for (EventDate eventDate : eventDates) {
                eventDate.setOccupiedBy(null);
                eventDate.getOwnerPlanItem().notifyMe(event, "");
            }

        }
        event.setAvailibleAfter(date);
        for (Prerequisite preq : mapOutcomeForDate.keySet()) {
            PlanItem asset = mapOutcomeForDate.get(preq);
            try {
                EventDate eventDate = eventDateRepository.findEventDateByOwnerPlanItemAndDate(asset, date);
                eventDate.setOccupiedBy(event);
                eventDate.setPrerequisite(preq);
                eventDateRepository.save(eventDate);
            } catch (NullPointerException e) {
                e.printStackTrace();
//                zakladamy, że jeśli brakuje daty, to zasob jest konieczny do wystapienia eventu, ale nie bierze w nim udziału
            }
        }
    }
}
