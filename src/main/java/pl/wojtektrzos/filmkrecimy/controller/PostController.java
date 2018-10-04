package pl.wojtektrzos.filmkrecimy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import pl.wojtektrzos.filmkrecimy.entity.Post;
import pl.wojtektrzos.filmkrecimy.repository.MovieRepository;
import pl.wojtektrzos.filmkrecimy.repository.PostRepository;
import pl.wojtektrzos.filmkrecimy.service.CurrentUser;
import pl.wojtektrzos.filmkrecimy.service.PostService;

@Controller
@RequestMapping("/Post")
public class PostController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    PostService postService;
    @Autowired
    MovieRepository movieRepository;

    @Secured("ROLE_USER")
    @PostMapping("/moviePost")
    public void addPostForMovie(Model model, @RequestAttribute String postContent, @RequestAttribute Long movieId, @AuthenticationPrincipal CurrentUser currentUser) {
        postService.addNewPostForMovie(postContent, currentUser.getUser(), movieRepository.getOne(movieId));
    }

    @Secured("ROLE_USER")
    @GetMapping("/getAllPostsForMovie")
    public String getAllPostsForMovie(){
        
        return null;
    }

}
