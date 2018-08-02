package pl.wojtektrzos.filmkrecimy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemRepository;
import pl.wojtektrzos.filmkrecimy.service.CurrentUser;

@Controller
public class HomeController {
    @Autowired
    PlanItemRepository planItemRepository;

    @GetMapping("/")
    @ResponseBody
    public String siema() {

        PlanItem planItem = planItemRepository.getOne(2l);
        return planItem.getPlanItemRoleNames() + "";
    }


    @Secured("ROLE_USER")
    @GetMapping("/hej")
    @ResponseBody
    public String testusera(@AuthenticationPrincipal CurrentUser currentUser) {
        return currentUser.getUser().getUsername();
    }
}
