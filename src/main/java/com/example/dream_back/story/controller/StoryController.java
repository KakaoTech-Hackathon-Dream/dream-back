package com.example.dream_back.story.controller;

import com.example.dream_back.story.dto.ReStoryReqDTO;
import com.example.dream_back.story.dto.StoryReqDTO;
import com.example.dream_back.story.dto.StoryResDTO;
import com.example.dream_back.story.service.StoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class StoryController {

    private final StoryService storyService;

    @RequestMapping("/story")
    public StoryResDTO createStory(@RequestBody StoryReqDTO storyReqDTO) {
        log.info("프론트 -> 백 데이터 수신");

        return storyService.saveStory(storyReqDTO);
    }

    @RequestMapping("/story/re")
    public StoryResDTO recreateStory(@RequestBody ReStoryReqDTO reStoryReqDTO){
        log.info("프론트 -> 백 데이터 재수신");

        return storyService.resaveStory(reStoryReqDTO);
    }
}
