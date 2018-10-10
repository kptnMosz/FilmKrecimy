package pl.wojtektrzos.filmkrecimy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wojtektrzos.filmkrecimy.dto.PostDto;
import pl.wojtektrzos.filmkrecimy.entity.Movie;
import pl.wojtektrzos.filmkrecimy.entity.Post;
import pl.wojtektrzos.filmkrecimy.entity.User;
import pl.wojtektrzos.filmkrecimy.entity.UserDetails;
import pl.wojtektrzos.filmkrecimy.repository.MovieRepository;
import pl.wojtektrzos.filmkrecimy.repository.PostRepository;
import pl.wojtektrzos.filmkrecimy.repository.UserDetailsRepository;
import pl.wojtektrzos.filmkrecimy.util.EnterLog;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    UserDetailsRepository userDetailsRepository;

    public void addNewPostForMovie(String postContent, UserDetails author, Movie movie) {
        addNewPost(postContent, author, movie);
    }

    public List<PostDto> getPostsForMovie(long movieId) {
        List<Post> preTranslatedPosts = postRepository.findAllByMovieIdOrderByCreatedDesc(movieId);
        return convertPostsToDto(preTranslatedPosts);
    }

    public List<PostDto> getPostsForUser(long userDetailsId) {
        List<Post> preTranslatedPosts = postRepository.findAllByAuthorId(userDetailsId);
        Set<Movie> myMovies = movieRepository.findDistinctByObserversContaining(userDetailsRepository.findUserDetailsById(userDetailsId).getPlanMyself());
        for(Movie movie: myMovies){
            preTranslatedPosts.addAll(postRepository.findAllByMovieIdOrderByCreatedDesc(movie.getId()));
        }
//ToDo add friends and posts for friends
        return convertPostsToDto(preTranslatedPosts);
    }

    private List<PostDto> convertPostsToDto(List<Post> preTranslatedPosts) {
        List<PostDto> postsToBeFedToPage = new ArrayList<>();
        for (Post post : preTranslatedPosts) {
            PostDto postDto = new PostDto();
            postDto.setCreated(post.getCreated());
            postDto.setPostText(post.getText());
            postDto.setAuthorsImageURI("/user/avatarfoto/" + post.getAuthor().getPlanMyself().getId());
            postDto.setAuthorsName(post.getAuthor().getPlanMyself().getName());
            postsToBeFedToPage.add(postDto);
        }
        Collections.sort(postsToBeFedToPage, new Comparator<PostDto>() {
            @Override
            public int compare(PostDto o1, PostDto o2) {
                return o2.getCreated().compareTo(o1.getCreated());
            }
        });
        return postsToBeFedToPage;
    }

    public void addNewPostForUser(String postContent, UserDetails author) {
        addNewPost(postContent, author, null);
    }

    private void addNewPost(String postContent, UserDetails author, Movie movie) {
        Post post = new Post();
        post.setText(postContent);
        post.setAuthor(author);
        post.setMovie(movie);
        post.setCreated(LocalDateTime.now());
        postRepository.save(post);
    }
}

