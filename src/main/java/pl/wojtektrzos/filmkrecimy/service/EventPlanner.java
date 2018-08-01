package pl.wojtektrzos.filmkrecimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.entity.EventDate;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.entity.Prerequisite;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemDao;
import pl.wojtektrzos.filmkrecimy.util.EnterLog;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventPlanner {
    @Autowired
    private PlanItemDao planItemDao;


    public EventPlanner() {
    }

    public Map<LocalDate, List<Prerequisite>> plan(long planItemId) {
        PlanItem planItem = planItemDao.getByIdWithAvailibleDatesPrerequisitesAndObservers(planItemId);

        List<Prerequisite> prerequisites = planItem.getPrerequisites();
        List<PlanItem> observers = planItem.getObservers();

//        -----====fill map with all availible planItems=====---------
        Map<LocalDate, List<Long>> dateMap = new HashMap<>();
        for (PlanItem observer : planItem.getObservers()) {
            List<EventDate> possibleDates = observer.getEventDates();
            for (EventDate possibleDate : possibleDates) {
                if (possibleDate.getOccupiedBy() == null || possibleDate.getOccupiedById() == planItemId) {
                    List<Long> availibleObservers = dateMap.get(possibleDate.getDate());
                    if (availibleObservers == null) {
                        availibleObservers = new ArrayList<>();
                    }
                    availibleObservers.add(observer.getId());
                    dateMap.put(possibleDate.getDate(), availibleObservers);
                }
            }
        }

        Map<LocalDate, List<Prerequisite>> untilOk = new HashMap<>();
//        ------====check prerequisites for each date===--------
        for (LocalDate date : dateMap.keySet()) {
            Map<Prerequisite, Long> mapOutcomeForDate = checkIfMeetsPrerequisits(dateMap.get(date), prerequisites);
            List<Prerequisite> missing = mapOutcomeForDate.keySet().stream().filter(p -> mapOutcomeForDate.get(p) == null).collect(Collectors.toList());
            if (missing == null) {
                //todo update done by date
                EnterLog.log("plan", "jestem w if sprawdzajacym czy data spenia wszystkie wymogi", date.toString());
                return untilOk;
            } else {
                EnterLog.log("plan", "jestem w else sprawdzajacym czy data spenia wszystkie wymogi", date.toString() + "\n" + untilOk.toString());
                untilOk.put(date, missing);
            }
        }
        return untilOk;
    }

    private Map<Prerequisite, Long> checkIfMeetsPrerequisits(List<Long> availibleObservers, List<Prerequisite> prerequisites) {
        Map<Prerequisite, Long> mapOutcomeForDate = new HashMap<>();
        for (Prerequisite prerequisite : prerequisites) {
            mapOutcomeForDate.put(prerequisite, null);
        }

        List<Prerequisite> preqByID = prerequisites.stream().filter(p -> p.getDiscriminator().equals("ID")).collect(Collectors.toList());
        EnterLog.log("checkIfMeetsPrerequisits", "zrzucam kolekcje prerequisiits by ID", preqByID.toString());
        EnterLog.log("checkIfMeetsPrerequisits", "i jeszcze pelna lista", prerequisites.toString());
        for (Prerequisite idPreq : preqByID) {
            Long presentObserver = null;
            try {
                presentObserver = availibleObservers.stream().filter(ob -> ob.equals(Long.parseLong(idPreq.getFieldValue()))).findAny().get();
            }catch(NoSuchElementException e){
                EnterLog.log("checkIfMeetsPrerequisits", "nie znaleziono elementu w zbiorze", availibleObservers.toString());
                e.printStackTrace();
            }
            mapOutcomeForDate.put(idPreq, presentObserver);
            if (presentObserver != null) {
                availibleObservers.remove(presentObserver);
            }
        }
        EnterLog.log("checkIfMeetsPrerequisits", "zwracam", mapOutcomeForDate.toString());
        return mapOutcomeForDate;
    }
}
