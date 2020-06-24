package com.example.demo.service;

import com.example.demo.dto.CommentsDto;
import com.example.demo.exception.PostNotFoundException;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.NotificationEmail;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentsService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
        public void save(CommentsDto commentsDto){
/*            User user = userRepository.findByUserName(commentsDto.getUserName())
                                    .orElseThrow(() -> new UsernameNotFoundException(commentsDto.getUserName()));*/

            Post post = postRepository.findById(commentsDto.getPostId())
                    .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));

              Comment comment =  commentMapper.map(commentsDto,post,authService.getCurrentUser());

                    commentRepository.save(comment);

                    // send mail to post's user

            String message = mailContentBuilder.build(post.getUser().getUserName() + " posted a comment on you post " );
            sendCommentNotification(message, post.getUser());
        }

    private void sendCommentNotification(String message, User user) {
            mailService.sendMail(new NotificationEmail(user.getUserName() +" commented on your post", user.getEmail(), message));
    }


    public List<CommentsDto> getAllCommentsforPost(Long id) {
                Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id.toString()));
                return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(Collectors.toList());

    }

    public List<CommentsDto> getAllCommentsforUser(String userName) {
            User user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new UsernameNotFoundException(userName));
            return  commentRepository.findAllByUser(user).stream().map(commentMapper::mapToDto).collect(Collectors.toList());
    }
}
