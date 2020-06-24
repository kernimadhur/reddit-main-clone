package com.example.demo.mapper;

import com.example.demo.dto.SubredditDto;
import com.example.demo.model.Post;
import com.example.demo.model.Subreddit;
import com.example.demo.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubredditMapper {

    @Mapping(target = "numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> noOfPosts){
        return noOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target= "posts", ignore = true)
    Subreddit mapDtoToSubreddit(SubredditDto subredditDto, User user);

}
