package pl.wojtektrzos.filmkrecimy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.wojtektrzos.filmkrecimy.dto.PostDto;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import pl.wojtektrzos.filmkrecimy.entity.Post;
import pl.wojtektrzos.filmkrecimy.repository.MovieRepository;
import pl.wojtektrzos.filmkrecimy.repository.PostRepository;
import pl.wojtektrzos.filmkrecimy.service.CurrentUser;
import pl.wojtektrzos.filmkrecimy.service.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

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
    @ResponseBody
    public void addPostForMovie(Model model, @AuthenticationPrincipal CurrentUser currentUser, HttpServletRequest req, @RequestParam String postContent,@RequestParam Long movieId) {
        postService.addNewPostForMovie(postContent.toString(), currentUser.getUser(), movieRepository.getOne(movieId));
    }

    @Secured("ROLE_USER")
    @GetMapping("/getAllPostsForMovie/{movieId}")
    @ResponseBody
    public List<PostDto> getAllPostsForMovie(@PathVariable long movieId){
        List posts = postService.getPostsForMovie(movieId);
        return posts;
    }

}
