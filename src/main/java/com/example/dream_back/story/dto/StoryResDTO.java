package com.example.dream_back.story.dto;

import com.example.dream_back.story.entity.StoryEntity;
import lombok.Data;

@Data
public class StoryResDTO {
    private int id;
    private String story;
    private boolean flag;

    public static StoryResDTO toStoryResDTO(AiStoryResDTO aiStoryResDTO){
        StoryResDTO storyResDTO = new StoryResDTO();

        storyResDTO.id = aiStoryResDTO.getId();
        storyResDTO.story = aiStoryResDTO.getStory();
        storyResDTO.flag = aiStoryResDTO.isFlag();

        return storyResDTO;
    }
}
