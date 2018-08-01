package pl.wojtektrzos.filmkrecimy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemDao;
import pl.wojtektrzos.filmkrecimy.service.EventPlanner;

@Controller
@RequestMapping("/test")
public class SortTestController {

    @Autowired
    PlanItemDao planItemDao;
    @Autowired
    EventPlanner eventPlanner;

    @GetMapping("/plan")
    @ResponseBody
    public String sortTest(Model model) {
        PlanItem planItem = planItemDao.getByIdWithAvailibleDatesPrerequisitesAndObservers(1L);

        return eventPlanner.plan(1).toString();
    }

    @GetMapping("/repo")
    @ResponseBody
    public String repoTest(Model model) {
        PlanItem planItem = planItemDao.getByIdWithAvailibleDatesPrerequisitesAndObservers(2L);

        return planItem.getEventDates().get(1)+ "";
    }
}
