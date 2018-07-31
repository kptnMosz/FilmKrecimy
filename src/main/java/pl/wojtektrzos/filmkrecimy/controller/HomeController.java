package pl.wojtektrzos.filmkrecimy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemRepository;

@Controller
public class HomeController {
    @Autowired
    PlanItemRepository planItemRepository;
    @GetMapping("/")
    @ResponseBody
    public String siema(){

        PlanItem planItem = planItemRepository.getOne(1l);
        return planItem.getId()+"";
    }
}
