package com.example.dream_back.story.dto;

import com.example.dream_back.story.entity.StoryEntity;
import lombok.Data;

@Data
public class AiStoryReqDTO {
    private int id;
    private String job;
    private String text;
    private String story;
    private int storyIndex;

    public static AiStoryReqDTO toAiStoryReqDTO(StoryEntity storyEntity) {
        AiStoryReqDTO aiStoryReqDTO = new AiStoryReqDTO();

        aiStoryReqDTO.id = storyEntity.getId();
        aiStoryReqDTO.job = storyEntity.getJob();
        aiStoryReqDTO.text = storyEntity.getText();
        aiStoryReqDTO.story = storyEntity.getStory();

        return aiStoryReqDTO;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public void setStoryIndex(int storyIndex) {
        this.storyIndex = storyIndex;
    }
}