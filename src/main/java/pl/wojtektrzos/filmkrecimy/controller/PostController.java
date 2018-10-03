package pl.wojtektrzos.filmkrecimy.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Post")
public class PostController {

    @Secured("ROLE_USER")
    @PostMapping("/moviePost")
    public void addPostForMovie(Model model){

    }
}
