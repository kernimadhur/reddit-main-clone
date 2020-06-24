package com.example.demo.service;

import com.example.demo.dto.PostRequest;
import com.example.demo.dto.PostResponse;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.exception.RedditException;
import com.example.demo.exception.RedditNotFoundException;
import com.example.demo.mapper.PostMapper;
import com.example.demo.model.Post;
import com.example.demo.model.Subreddit;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.SubredditRepository;
import com.example.demo.repository.UserRepository;
import javafx.geometry.Pos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Data
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    public void save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                     .orElseThrow(() -> new RedditNotFoundException("No subreddit found by name : " + postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest,subreddit,authService.getCurrentUser()));

        // postRepository.save(postRequest);
    }

    public PostResponse getPost(Long id){
        Post post =  postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));
                return postMapper.mapToDto(post);
    }

    public List<PostResponse> getAllPosts(){
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsBySubreddit(Long subredditId){
        Subreddit subreddit =subredditRepository.findById(subredditId).orElseThrow(() -> new RedditNotFoundException(subredditId.toString()));
                List<Post> posts = postRepository.findAllBySubreddit(subreddit);
                return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());

    }

    public List<PostResponse> getPostsByUsername(String userName){
        User user = userRepository.findByUserName(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
            return  postRepository
                .findByUser(user)
                .stream().map(postMapper::mapToDto)
                .collect(Collectors.toList());

    }

}
