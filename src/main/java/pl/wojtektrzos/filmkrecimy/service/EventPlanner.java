package pl.wojtektrzos.filmkrecimy.service;

import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EventPlanner {
    public EventPlanner() {
    }

    public void plan(PlanItem planItem) {
        Map<LocalDate, ArrayList<Long>> availibledates = new HashMap<>();
        planItem.getPrerequisites();

    }
}
