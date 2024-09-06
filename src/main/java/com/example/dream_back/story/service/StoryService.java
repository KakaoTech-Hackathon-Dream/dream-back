package com.example.dream_back.story.service;

import com.example.dream_back.story.entity.StoryEntity;
import com.example.dream_back.story.dto.*;
import com.example.dream_back.story.repositoty.StoryRepository;
import com.example.dream_back.story.valid.StoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;



@Service
@Slf4j
public class StoryService {
    private final StoryRepository storyRepository;
    private final WebClient webClient;

    private static final String AI_URI = "http://ai:8000";

    public StoryService(StoryRepository storyRepository, WebClient.Builder webClientBuilder) {
        this.storyRepository = storyRepository;
        this.webClient = webClientBuilder.build();
    }

    @Transactional
    public StoryResDTO saveStory(StoryReqDTO storyReqDTO) {
        StoryEntity storyEntity = StoryEntity.toStoryEntity(storyReqDTO);
        storyRepository.save(storyEntity);

        AiStoryReqDTO aiStoryReqDTO = AiStoryReqDTO.toAiStoryReqDTO(storyEntity);
        aiStoryReqDTO.setStory(null);
        aiStoryReqDTO.setStoryIndex(0);

        AiStoryResDTO aiStoryResDTO = webClient.post()
                .uri(AI_URI + "/ai/story")
                .bodyValue(aiStoryReqDTO)
                .retrieve()
                .bodyToMono(AiStoryResDTO.class)
                .block();

        storyEntity.setStory(aiStoryResDTO.getStory());
        storyEntity.setStoryIndex(aiStoryResDTO.getStoryIndex());
        storyRepository.save(storyEntity);

        return StoryResDTO.toStoryResDTO(aiStoryResDTO);
    }

    @Transactional
    public StoryResDTO resaveStory(ReStoryReqDTO reStoryReqDTO) {
        StoryEntity storyEntity = storyRepository.findById(reStoryReqDTO.getId())
                .orElseThrow(() -> new StoryNotFoundException(reStoryReqDTO.getId()));

        AiStoryReqDTO aiStoryReqDTO = AiStoryReqDTO.toAiStoryReqDTO(storyEntity);
        aiStoryReqDTO.setId(reStoryReqDTO.getId());
        aiStoryReqDTO.setStoryIndex(reStoryReqDTO.getStoryIndex());

        AiStoryResDTO aiStoryResDTO = webClient.post()
                .uri(AI_URI + "/ai/story")
                .bodyValue(aiStoryReqDTO)
                .retrieve()
                .bodyToMono(AiStoryResDTO.class)
                .block();

        storyEntity.addStory(aiStoryResDTO.getStory());
        storyEntity.setStoryIndex(aiStoryResDTO.getStoryIndex());
        storyRepository.save(storyEntity);

        return StoryResDTO.toStoryResDTO(aiStoryResDTO);
    }

}