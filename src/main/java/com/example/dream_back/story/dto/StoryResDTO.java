package com.example.dream_back.story.dto;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class StoryResDTO {
    private int id;
    @Lob
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
