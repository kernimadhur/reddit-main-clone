package com.example.demo.service;

import com.example.demo.dto.SubredditDto;
import com.example.demo.exception.RedditException;
import com.example.demo.mapper.SubredditMapper;
import com.example.demo.model.Subreddit;
import com.example.demo.model.User;
import com.example.demo.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    private final AuthService authService;

    @Transactional
    public Subreddit save(SubredditDto subredditDto){   // some issue, user's whole object is shown in response along with pass.
        User user = authService.getCurrentUser();          // TODO: 25-06-2020 user shouldnt comeeee
        Subreddit subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto,user));
         subredditDto.setId(subreddit.getId());
        return subreddit;
    }

   /* private Subreddit mapSubredditDto(SubredditDto subredditDto) {
         return Subreddit.builder().name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .build();
    }
    *Commented because started using Mapstruct*
    */

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditDto)
        .collect(Collectors.toList());
    }

  /*  private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder().name(subreddit.getName())
                .id(subreddit.getId())
                .numberOfPosts(subreddit.getPosts().size())
                .build();

    }*/

  public SubredditDto getSubreddit(Long id){
      Subreddit subreddit = subredditRepository.findById(id)
                                .orElseThrow(() -> new RedditException("No Subreddit found by this id"));
      return subredditMapper.mapSubredditDto(subreddit);

         }
}
