package pl.wojtektrzos.filmkrecimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.entity.EventDate;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.entity.Prerequisite;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemDao;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventPlanner {
    @Autowired
    private PlanItemDao planItemDao;
    @Autowired
    private EventUpdaterService eventUpdaterService;

    public EventPlanner() {
    }

    public Map<LocalDate, Map<Prerequisite, PlanItem>> updatePlan(long planItemId) {
        PlanItem event = planItemDao.getByIdWithAvailibleDatesPrerequisitesAndObservers(planItemId);

        Map<LocalDate, List<PlanItem>> dateMap = getAvailibleResources(event);
        List<Prerequisite> prerequisites = event.getPrerequisites();

        //        ------====check prerequisites for each date===--------
        for (LocalDate date : dateMap.keySet().stream().sorted().collect(Collectors.toList())) {
            Map<Prerequisite, PlanItem> mapOutcomeForDate = checkIfDateMeetsPrerequisits(dateMap.get(date), prerequisites);
            List<Prerequisite> missing = mapOutcomeForDate.keySet().stream().filter(p -> mapOutcomeForDate.get(p) == null).collect(Collectors.toList());
            if (missing.isEmpty()) {
                Map<LocalDate, Map<Prerequisite, PlanItem>> outcome = new HashMap<>();
                outcome.put(date,mapOutcomeForDate);
                eventUpdaterService.updateEvent(event, date, mapOutcomeForDate);
                return outcome;
            }
        }

        return null;
    }

    public Map<LocalDate, List<PlanItem>> getAvailibleResources(PlanItem event) {
        List<Prerequisite> prerequisites = event.getPrerequisites();
        Map<LocalDate, List<PlanItem>> dateMap = new HashMap<>();
        fillDateMapWithItemsPerAvailibility(event, dateMap);
        fillDateMapWithItemsPerBeforeandAfter(event, dateMap);
        return dateMap;
    }

    private void fillDateMapWithItemsPerAvailibility(PlanItem event, Map<LocalDate, List<PlanItem>> dateMap) {
        for (PlanItem observer : event.getObservers().stream().filter(p -> p.getAvailibleAfter() == null && p.getAvailibleBefore() == null).collect(Collectors.toList())) {
            List<EventDate> possibleDates = observer.getEventDates();
            for (EventDate possibleDate : possibleDates) {
                if (possibleDate.getOccupiedBy() == null || possibleDate.getOccupiedById() == event.getId()) {
                    List<PlanItem> availibleObservers = dateMap.get(possibleDate.getDate());
                    if (availibleObservers == null) {
                        availibleObservers = new ArrayList<>();
                    }
                    availibleObservers.add(observer);
                    dateMap.put(possibleDate.getDate(), availibleObservers);
                }
            }
        }
    }

    private void fillDateMapWithItemsPerBeforeandAfter(PlanItem event, Map<LocalDate, List<PlanItem>> dateMap) {
         for (PlanItem observer : event.getObservers().stream()
                .filter(p -> p.getAvailibleAfter() != null || p.getAvailibleBefore() != null)
                .collect(Collectors.toList())) {
            LocalDate min = null, max = null;
            if (observer.getAvailibleBefore() == null) {
                try {
                    max = dateMap.keySet().stream().max(Comparator.comparing(LocalDate::toEpochDay)).get();
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }
            } else {
                max = observer.getAvailibleBefore();
            }
            if (observer.getAvailibleAfter() == null) {

                try {
                    min = dateMap.keySet().stream().min(Comparator.comparing(LocalDate::toEpochDay)).get();
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }
            } else {
                min = observer.getAvailibleAfter();
            }
            if (min == null) {
                min = max;
            }
            if (max == null) {
                max = min;
            }

            for (LocalDate date : dateMap.keySet()) {
                if (date.isAfter(min.minusDays(1)) && date.isBefore(max.plusDays(1))) {
                    dateMap.get(date).add(observer);
                }
            }
        }
    }

    private Map<Prerequisite, PlanItem> checkIfDateMeetsPrerequisits(List<PlanItem> availibleObservers, List<Prerequisite> prerequisites) {
        Map<Prerequisite, PlanItem> mapOutcomeForDate = new HashMap<>();
        for (Prerequisite prerequisite : prerequisites) {
            mapOutcomeForDate.put(prerequisite, null);
        }
//-----====najpierw planujemy userow by ID====--------
        List<Prerequisite> preqByID = prerequisites.stream().filter(p -> p.getDiscriminator().equals("ID")).collect(Collectors.toList());
        for (Prerequisite idPreq : preqByID) {
            PlanItem presentObserver = null;
            try {
                presentObserver = availibleObservers.stream()
                        .filter(ob -> Long.valueOf(ob.getId()).equals(Long.parseLong(idPreq.getFieldValue())))
                        .findAny().get();
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
            mapOutcomeForDate.put(idPreq, presentObserver);
            if (presentObserver != null) {
                availibleObservers.remove(presentObserver);
            }
        }
//-----====teraz zrzucamy userow by Role====--------
        List<Prerequisite> preqByRole = prerequisites.stream().filter(p -> !p.getDiscriminator().equals("ID")).collect(Collectors.toList());
        for (Prerequisite rolePreq : preqByRole) {
            PlanItem presentObserver = null;
            try {
                presentObserver = availibleObservers.stream().filter(ob -> ob.getPlanItemRoleNames().contains(rolePreq.getFieldValue()))
                        .findAny().get();
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
            mapOutcomeForDate.put(rolePreq, presentObserver);
            if (presentObserver != null) {
                availibleObservers.remove(presentObserver);
            }
        }

        return mapOutcomeForDate;
    }
}
