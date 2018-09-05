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

    public Map<Integer, Integer> getNumberOfDaysInMonth() {
        Map<Integer, Integer> numberOfDaysInMonth = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            LocalDate now = LocalDate.now();
            int year = now.getYear();
            if (i < now.getMonth().getValue()) {
                year = year + 1;
            }
            LocalDate lastDayOfMonth;
            if (i == 12) {
                lastDayOfMonth = LocalDate.of(year, 1, 1).minusDays(1);
            } else {
                lastDayOfMonth = LocalDate.of(year, i + 1, 1).minusDays(1);
            }
            int daysInMonth = lastDayOfMonth.getDayOfMonth();
            numberOfDaysInMonth.put(i, daysInMonth);
        }
        return numberOfDaysInMonth;
    }

    public List<LocalDate> getYear() {
        List<LocalDate> outcome = new ArrayList<>();
        for (int i = 0; i < 365; i++) {
            outcome.add(LocalDate.now().plusDays(i));
        }
        return outcome;
    }

    public String updateAvailibleDates(Set<LocalDate> availibleDates, PlanItem targetPlanItem){
        String errorLog =null;

        for(LocalDate date:availibleDates)
        {
            EventDate eventDate = eventDateRepository.findEventDateByOwnerPlanItemAndDate(targetPlanItem, date);
            if(eventDate==null){
                eventDate=new EventDate(date,targetPlanItem);
                eventDateRepository.save(eventDate);
            }
        }

        List<EventDate> targetsEventDates = eventDateRepository.findAllByOwnerPlanItem(targetPlanItem);
        for (EventDate eventDate:targetsEventDates)
        {
            if(!availibleDates.contains(eventDate.getDate())){
                if(eventDate.getOccupiedBy()==null){
                    eventDateRepository.delete(eventDate);
                }else {
                    errorLog += eventDate.getDate().toString();
                }
            }
        }

        return errorLog;
    }

}
