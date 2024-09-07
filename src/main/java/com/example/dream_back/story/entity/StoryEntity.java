package com.example.dream_back.story.entity;

import com.example.dream_back.story.dto.StoryReqDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StoryEntity {
    @Id
    @GeneratedValue
    private int id;

    private String job;

    @Column(columnDefinition = "LONGTEXT")
    private String text;

    @Column(columnDefinition = "LONGTEXT")
    private String story;

    private int storyIndex;

    public static StoryEntity toStoryEntity(StoryReqDTO storyReqDTO){
        StoryEntity storyEntity = new StoryEntity();

        storyEntity.job = storyReqDTO.getJob();
        storyEntity.text = storyReqDTO.getText();
        storyEntity.story = null;

        return storyEntity;
    }

    public void addStory(String story){
        this.story = this.story + story;
    }

    public void setStoryIndex(int index){
        this.storyIndex = index;
    }

    public void setStory(String story){
        this.story = story;
    }
}
