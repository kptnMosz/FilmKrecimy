package pl.wojtektrzos.filmkrecimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.dto.PostDto;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import pl.wojtektrzos.filmkrecimy.entity.Post;
import pl.wojtektrzos.filmkrecimy.entity.User;
import pl.wojtektrzos.filmkrecimy.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public void addNewPostForMovie(String postContent, User author, Movie movie) {
        Post post = new Post();
        post.setText(postContent);
        post.setAuthor(author);
        post.setMovie(movie);
        post.setCreated(LocalDateTime.now());
        postRepository.save(post);
    }

    public List<PostDto> getPostsForMovie(long movieId) {
        List<Post> preTranslatedPosts = postRepository.findAllByMovieIdOrderByCreatedDesc(movieId);
        List<PostDto> postsToBeFedToPage = new ArrayList<>();
        for (Post post : preTranslatedPosts) {
            PostDto postDto = new PostDto();
            postDto.setCreated(post.getCreated());
            postDto.setPostText(post.getText());
            postDto.setAuthorsImageURI("/user/avatarfoto/"+post.getAuthor().getDetails().getPlanMyself().getId());
            postDto.setAuthorsName(post.getAuthor().getDetails().getPlanMyself().getName());
            postsToBeFedToPage.add(postDto);
        }
        return postsToBeFedToPage;
    }
}

