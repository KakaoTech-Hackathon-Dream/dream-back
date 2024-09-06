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
        // 1. 먼저 StoryEntity를 저장
        StoryEntity storyEntity = StoryEntity.toStoryEntity(storyReqDTO);
        storyRepository.save(storyEntity);

        // 2. 트랜잭션 외부에서 AI API 호출 처리
        AiStoryResDTO aiStoryResDTO = callAiService(storyEntity);

        // 3. AI 결과를 기반으로 다시 StoryEntity 업데이트
        storyEntity.setStory(aiStoryResDTO.getStory());
        storyEntity.setStoryIndex(aiStoryResDTO.getStoryIndex());
        storyRepository.save(storyEntity);

        return StoryResDTO.toStoryResDTO(aiStoryResDTO);
    }

    private AiStoryResDTO callAiService(StoryEntity storyEntity) {
        AiStoryReqDTO aiStoryReqDTO = AiStoryReqDTO.toAiStoryReqDTO(storyEntity);
        aiStoryReqDTO.setStory(null); // 초기 값 설정
        aiStoryReqDTO.setStoryIndex(0); // 초기 값 설정

        // WebClient를 통한 외부 API 호출
        return webClient.post()
                .uri(AI_URI + "/ai/story")
                .bodyValue(aiStoryReqDTO)
                .retrieve()
                .bodyToMono(AiStoryResDTO.class)
                .block();
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

        log.info("----------------");
        log.info(storyEntity.getStory());
        log.info("----------------");
        log.info(aiStoryResDTO.getStory());

        storyEntity.addStory(aiStoryResDTO.getStory());
        storyEntity.setStoryIndex(aiStoryResDTO.getStoryIndex());
        storyRepository.save(storyEntity);

        return StoryResDTO.toStoryResDTO(aiStoryResDTO);
    }

}