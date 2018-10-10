package pl.wojtektrzos.filmkrecimy.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.wojtektrzos.filmkrecimy.entity.EventDate;
import pl.wojtektrzos.filmkrecimy.entity.PlanItem;
import pl.wojtektrzos.filmkrecimy.entity.User;
import pl.wojtektrzos.filmkrecimy.entity.UserDetails;
import pl.wojtektrzos.filmkrecimy.repository.EventDateRepository;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemRepository;
import pl.wojtektrzos.filmkrecimy.repository.PlanItemRoleRepository;
import pl.wojtektrzos.filmkrecimy.repository.UserDetailsRepository;
import pl.wojtektrzos.filmkrecimy.service.AvatarService;
import pl.wojtektrzos.filmkrecimy.service.CurrentUser;
import pl.wojtektrzos.filmkrecimy.service.UserCalendarService;
import pl.wojtektrzos.filmkrecimy.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/user")
@MultipartConfig(maxFileSize = 1024 * 1024 * 40, maxRequestSize = 1024 * 1024 * 50, fileSizeThreshold = 1024 * 1024 * 20)
public class UserController {
    @Autowired
    UserServiceImpl userRepository;
    @Autowired
    PlanItemRepository planItemRepository;
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    PlanItemRoleRepository planItemRoleRepository;
    @Autowired
    UserCalendarService userCalendarService;
    @Autowired
    EventDateRepository eventDateRepository;
    @Autowired
    AvatarService avatarService;
    private String SAVE_DIR = "avatars";


    @GetMapping("/register")

    public String initAddNewUser(Model model) {
        model.addAttribute("user", new User());
        return "views/user/register";
    }

    @PostMapping("/register")
    public String addNewUser(@Validated User user, BindingResult result) {
        if (result.hasErrors()) {
            return "views/user/register";
        }
        userRepository.saveUser(user);

        return "redirect:/login";
    }

    @GetMapping("/edit")
    @Secured("ROLE_USER")
    public String editUser(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        model.addAttribute("details", userDetailsRepository.findUserDetailsByLogInfo(currentUser.getUser()));
        model.addAttribute("planItemRoles", planItemRoleRepository.findAllByModifier("USERTAG"));
        return "views/user/edit";
    }

    @PostMapping("/edit")
    @Secured("ROLE_USER")
    public String updateUser(@AuthenticationPrincipal CurrentUser currentUser, @Validated UserDetails details, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "views/user/edit";
        }
        currentUser.getUser().getDetails().setEmail(details.getEmail());
        userDetailsRepository.save(currentUser.getUser().getDetails());
        redirectAttributes.addFlashAttribute("message", "zaktualizowano email");
        return "redirect:/user/edit";
    }

    @GetMapping("/projects")
    @Secured("ROLE_USER")
    public String userProjects(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        List<EventDate> eventDates = eventDateRepository.findAllByOwnerPlanItem(currentUser.getUser().getDetails().getPlanMyself());
        UserDetails user = currentUser.getUser().getDetails();
        model.addAttribute("user", user);
        model.addAttribute("calendar", userCalendarService.getYear());
        model.addAttribute("daysInMonth", userCalendarService.getNumberOfDaysInMonth());
        model.addAttribute("weekDays", new int[]{1, 2, 3, 4, 5, 6, 7});
        model.addAttribute("maxNumberOfWeeksInMonth", new int[]{1, 2, 3, 4, 5, 6});
        model.addAttribute("numberOfMonthsInScope", new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        model.addAttribute("eventDates", eventDateRepository.findAllByOwnerPlanItemAndOccupiedByIsNotNull(user.getPlanMyself()));
        model.addAttribute("occupiedDates", userCalendarService.stringifyEventDates(eventDates));

        return "views/user/projects";
    }

    @PostMapping("/updateAvailibleDates")
    @Secured("ROLE_USER")
    @ResponseBody
    public String updateAvailibleDates(@RequestParam String[] eventDates, @AuthenticationPrincipal CurrentUser currentUser) {
        Set<LocalDate> newAvailibleDates = new HashSet<>();
        for (String unparsedDate : eventDates) {
            newAvailibleDates.add(LocalDate.parse(unparsedDate));
        }
        return userCalendarService.updateAvailibleDates(newAvailibleDates, currentUser.getUser().getDetails().getPlanMyself());
    }

    @PostMapping("/updatePicture")
    @ResponseBody
    @Secured("ROLE_USER")
    public String updateProfilePicture(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal CurrentUser currentUser) throws IOException, ServletException {

        String saveDir = request.getServletContext().getRealPath("");
        String targetFile = saveDir + SAVE_DIR;
        File folder = new File(targetFile);
        File tmpFile;

        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdir();
        }
        String test = "";
        for (Part part : request.getParts()) {

            try {
                if (part.getContentType().contains("image")) {
                    tmpFile = new File(targetFile + File.separator + part.getSubmittedFileName());
                    part.write(tmpFile.getPath());
                    test = avatarService.updateAvatarPicture(tmpFile, currentUser.getUser().getDetails().getPlanMyself());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        return test;
    }

    @GetMapping("/avatarfoto/my")
    @Secured("ROLE_USER")
    public void getAvatarFoto(HttpServletResponse resp, HttpServletRequest request, @AuthenticationPrincipal CurrentUser currentUser) throws IOException {
        avatarService.placeAvatarFotoInOutput(resp,request,currentUser.getUser().getDetails().getPlanMyself());
    }

    @GetMapping("/avatarfoto/{planItemId}")
    @Secured("ROLE_USER")
    public void getAvatarFoto(HttpServletResponse resp, HttpServletRequest request, @PathVariable long planItemId) throws IOException {

        avatarService.placeAvatarFotoInOutput(resp,request,planItemRepository.findPlanItemById(planItemId));
    }

    @GetMapping("/showWall")
    @Secured("ROLE_USER")
    public String showWall(Model model, @AuthenticationPrincipal CurrentUser currentUser)
    {
        model.addAttribute("user", currentUser.getUser().getDetails());
        return "views/user/wall";
    }




}
