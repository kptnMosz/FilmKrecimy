package pl.wojtektrzos.filmkrecimy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemDao;

@Controller
@RequestMapping("/test")
public class SortTestController {

    @Autowired
    PlanItemDao planItemDao;

    @GetMapping("/")
    @ResponseBody
    public String sortTest() {
        return planItemDao.getByIdWithAvailibleDates(2).toString();
    }
}
