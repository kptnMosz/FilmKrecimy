package pl.wojtektrzos.filmkrecimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import pl.wojtektrzos.filmkrecimy.entity.Post;
import pl.wojtektrzos.filmkrecimy.entity.User;
import pl.wojtektrzos.filmkrecimy.repository.PostRepository;

import java.time.LocalDateTime;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    public void addNewPostForMovie(String postContent, User author, Movie movie) {
        Post post = new Post();
        post.setText(postContent);
        post.setAuthor(author);
        post.setCreated(LocalDateTime.now());
        postRepository.save(post);
    }
}

