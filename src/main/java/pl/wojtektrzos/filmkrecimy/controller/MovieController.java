package pl.wojtektrzos.filmkrecimy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.wojtektrzos.filmkrecimy.dto.ActivityDto;
import pl.wojtektrzos.filmkrecimy.entity.Activity;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.entity.Prerequisite;
import pl.wojtektrzos.filmkrecimy.repository.*;
import pl.wojtektrzos.filmkrecimy.service.ActivityService;
import pl.wojtektrzos.filmkrecimy.service.CurrentUser;
import pl.wojtektrzos.filmkrecimy.service.EventPlanner;
import pl.wojtektrzos.filmkrecimy.util.EnterLog;


@Controller
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    PlanItemRoleRepository planItemRoleRepository;
    @Autowired
    ActivityService activityService;
    @Autowired
    PlanItemRepository planItemRepository;
    @Autowired
    EventPlanner eventPlanner;
    @Autowired
    PrerequisiteRepository prerequisiteRepository;

    @Secured("ROLE_USER")
    @GetMapping("/addmovie")
    public String newMovie(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        Movie movie = new Movie();
        movie.setOwner(currentUser.getUser().getDetails());
        model.addAttribute("editable", true);
        model.addAttribute("movie", movie);
        return "views/movie/cumovie";
    }

    @Secured("ROLE_USER")
    @PostMapping("/addmovie")
    public String newMovie(@AuthenticationPrincipal CurrentUser currentUser, Model model, @Validated Movie movie, BindingResult result) {
        if (result.hasErrors()) {
            return "views/movie/cumovie";
        }
        if (movie.getOwner().getId() == currentUser.getUser().getDetails().getId()) {
            model.addAttribute("editable", true);
        }

        movieRepository.save(movie);

        return "redirect:myMovies";
    }

    @Secured("ROLE_USER")
    @GetMapping("/myMovies")
    public String myMovies(Model model, @AuthenticationPrincipal CurrentUser currentUser) {
        model.addAttribute("movies", movieRepository.findAllByOwner(currentUser.getUser().getDetails()));
        return "views/movie/myMovies";
    }

    @Secured("ROLE_USER")
    @GetMapping("/moviedetails/{id}/")
    public String moviedetails(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable long id, Model model) {
        Movie movie = movieRepository.getOne(id);
        if (movie.getOwner().getId() == currentUser.getUser().getDetails().getId()) {
            model.addAttribute("editable", true);
        }
        model.addAttribute("movie", movie);
        model.addAttribute("activities", activityRepository.findAllByMovie(movie));
        return "views/movie/cumovie";
    }

    @Secured("ROLE_USER")
    @GetMapping("/addSceene/{movieId}")
    public String addScene(@AuthenticationPrincipal CurrentUser currentUser, Model model, @PathVariable long movieId) {
        EnterLog.log("addScene", "numer filmu", movieId + "");
        Movie movie = movieRepository.getOne(movieId);
        if (movie.getOwner().getId() == currentUser.getUser().getDetails().getId()) {
            model.addAttribute("editable", true);
            ActivityDto activityDto = new ActivityDto();
            activityDto.setMovie(movie);
            activityDto.setNumberOfDays(1);
            model.addAttribute("activity", activityDto);
            model.addAttribute("movie", movie);
            model.addAttribute("events", planItemRoleRepository.findAllByModifier("EVENT"));
        } else {
            model.addAttribute("notauthorised", true);
        }
        return "views/movie/activityDetails";

    }

    @Secured("ROLE_USER")
    @PostMapping("/addSceene/{movieId}")
    public String registerScene(@AuthenticationPrincipal CurrentUser currentUser, Model model, ActivityDto activityDto, @PathVariable long movieId) {
        EnterLog.log("registerScene", "numer filmu", activityDto.toString());
        EnterLog.log("registerScene", "otrzymany obiekt", movieId + "");

        Movie movie = movieRepository.getOne(movieId);
        if (movie.getOwner().getId() == currentUser.getUser().getDetails().getId()) {
            model.addAttribute("editable", true);
            model.addAttribute("activity", new ActivityDto());
            model.addAttribute("movie", movie);
            model.addAttribute("events", planItemRoleRepository.findAllByModifier("EVENT"));
        } else {
            model.addAttribute("notauthorised", true);
            return "views/movie/activityDetails";
        }
        activityService.registerNewActivity(activityDto);
        return "redirect:/movie/moviedetails/" + movie.getId() + "/";

    }

    @Secured("ROLE_USER")
    @GetMapping("/observe/{sceeneId}")
    public String observe(@PathVariable long sceeneId, @AuthenticationPrincipal CurrentUser currentUser) {
        Activity activity = activityRepository.getOne(sceeneId);

        for (PlanItem event : activity.getEvents()) {
            event.addObserver(currentUser.getUser().getDetails().getPlanMyself());
            EnterLog.log("observe", "dane", activity.toString() + " \n" + event.toString());

            planItemRepository.save(event);
        }
        return "redirect:/movie/moviedetails/" + activity.getMovie().getId() + "/";
    }

    @Secured("ROLE_USER")
    @GetMapping("/plan/{sceeneId}")
    public String planEvent(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable long sceeneId) {
        Activity activity = activityRepository.getOne(sceeneId);

        for (PlanItem event : activity.getEvents()) {

            eventPlanner.updatePlan(event.getId());
        }
        return "redirect:/movie/moviedetails/" + activity.getMovie().getId() + "/";
    }

    @Secured("ROLE_USER")
    @GetMapping("/editSceene/{sceeneId}")
    public String editScene(@AuthenticationPrincipal CurrentUser currentUser, @PathVariable long sceeneId, Model model) {
        Activity activity = activityRepository.getOne(sceeneId);

        model.addAttribute("activity", activity.getEvents());
        model.addAttribute("preq", new Prerequisite());


        return "views/movie/activityAddPrerequisites";
    }

    @Secured("ROLE_USER")
    @PostMapping("/addPrerequisite/{sceeneId}")
    public String addPreq(@AuthenticationPrincipal CurrentUser currentUser, Prerequisite prerequisite, @PathVariable long sceeneId)
    {
        PlanItem event = planItemRepository.getOne(sceeneId);
        event.addPrerequisites(prerequisite);

        prerequisiteRepository.save(prerequisite);
        planItemRepository.save(event);

        return "redirect:/movie/moviedetails/" + event.getActivity().getMovie().getId() + "/";
    }
}