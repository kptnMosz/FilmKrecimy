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
        Map<LocalDate, List<PlanItem>> dateMap = new HashMap<>();
        for (PlanItem observer : planItem.getObservers()) {
            List<EventDate> possibleDates = observer.getEventDates();
            for (EventDate possibleDate : possibleDates) {
                if (possibleDate.getOccupiedBy() == null || possibleDate.getOccupiedById() == planItemId) {
                    List<PlanItem> availibleObservers = dateMap.get(possibleDate.getDate());
                    if (availibleObservers == null) {
                        availibleObservers = new ArrayList<>();
                    }
                    availibleObservers.add(observer);
                    dateMap.put(possibleDate.getDate(), availibleObservers);
                }
            }
        }
        EnterLog.log("plan", "nasza mapa:", dateMap.toString());
        EnterLog.log("plan", "nasze prerekwizyty:", prerequisites.toString());


        Map<LocalDate, List<Prerequisite>> untilOk = new HashMap<>();
//        ------====check prerequisites for each date===--------
        for (LocalDate date : dateMap.keySet().stream().sorted().collect(Collectors.toList())) {
            Map<Prerequisite, PlanItem> mapOutcomeForDate = checkIfMeetsPrerequisits(dateMap.get(date), prerequisites);
            List<Prerequisite> missing = mapOutcomeForDate.keySet().stream().filter(p -> mapOutcomeForDate.get(p) == null).collect(Collectors.toList());
            EnterLog.log("plan", "mamy taki missing/full map teraz:", missing.toString()+"\n"+mapOutcomeForDate.toString());
            if (missing.isEmpty()) {
                //todo update done by date
                EnterLog.log("plan", "nasza mapa:", dateMap.toString());
                EnterLog.log("plan", date + "mamy sukces: jestem w if sprawdzajacym czy data spenia wszystkie wymogi", date.toString());
                return untilOk;
            } else {
                EnterLog.log("plan", date + "jestem w else sprawdzajacym czy data spenia wszystkie wymogi", date.toString() + "\n" + untilOk.toString());
                untilOk.put(date, missing);
            }
        }

        return untilOk;
    }

    private Map<Prerequisite, PlanItem> checkIfMeetsPrerequisits(List<PlanItem> availibleObservers, List<Prerequisite> prerequisites) {
        Map<Prerequisite, PlanItem> mapOutcomeForDate = new HashMap<>();
        for (Prerequisite prerequisite : prerequisites) {
            mapOutcomeForDate.put(prerequisite, null);
        }
//-----====najpierw planujemy userow by ID====--------
        List<Prerequisite> preqByID = prerequisites.stream().filter(p -> p.getDiscriminator().equals("ID")).collect(Collectors.toList());
        EnterLog.log("checkIfMeetsPrerequisits", "zrzucam kolekcje prerequisiits by ID", preqByID.toString());
        for (Prerequisite idPreq : preqByID) {
            PlanItem presentObserver = null;
            try {
                presentObserver = availibleObservers.stream()
                        .filter(ob -> Long.valueOf(ob.getId()).equals(Long.parseLong(idPreq.getFieldValue())))
                        .findAny().get();
            }catch(NoSuchElementException e){
                e.printStackTrace();
            }
            mapOutcomeForDate.put(idPreq, presentObserver);
            if (presentObserver != null) {
                availibleObservers.remove(presentObserver);
            }
        }
//-----====teraz zrzucamy userow by Role====--------
        List<Prerequisite> preqByRole = prerequisites.stream().filter(p -> !p.getDiscriminator().equals("ID")).collect(Collectors.toList());
        EnterLog.log("preqByRole", "zrzucam kolekcje prerequisits nie po ID", preqByRole.toString());
        for (Prerequisite rolePreq : preqByRole) {
            PlanItem presentObserver = null;
            try {
                presentObserver = availibleObservers.stream().filter(ob -> ob.getPlanItemRoleNames().contains(rolePreq.getFieldValue()))
                        .findAny().get();
            }catch(NoSuchElementException e){
                EnterLog.log("preqByRole", "nie znaleziono elementu w zbiorze", availibleObservers.toString());
                e.printStackTrace();
            }
            mapOutcomeForDate.put(rolePreq, presentObserver);
            if (presentObserver != null) {
                availibleObservers.remove(presentObserver);
            }
        }


        EnterLog.log("checkIfMeetsPrerequisits", "zwracam", mapOutcomeForDate.toString());
        return mapOutcomeForDate;
    }
}
