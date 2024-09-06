package com.example.dream_back.service;

import com.example.dream_back.story.dto.*;
import com.example.dream_back.story.entity.StoryEntity;
import com.example.dream_back.story.repositoty.StoryRepository;
import com.example.dream_back.story.service.StoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StoryServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private StoryService storyService;

    @BeforeEach
    void setUp() {
        // 테스트를 실행하기 전에 Mock 객체를 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveStoryTest() {
        // Given: 테스트 데이터 설정
        StoryReqDTO storyReqDTO = new StoryReqDTO();
        storyReqDTO.setJob("Engineer");
        storyReqDTO.setText("Sample text");

        // StoryEntity를 생성
        StoryEntity storyEntity = new StoryEntity();
        storyEntity.setJob("Engineer");  // setter 사용
        storyEntity.setText("Sample text");

        // AI 서버에서 응답할 데이터를 설정
        AiStoryResDTO aiStoryResDTO = new AiStoryResDTO();
        aiStoryResDTO.setId(1);
        aiStoryResDTO.setStory("AI generated story");
        aiStoryResDTO.setStoryIndex(1);

        // StoryRepository와 WebClient의 동작을 Mock으로 설정
        when(storyRepository.save(any(StoryEntity.class))).thenReturn(storyEntity);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(AiStoryResDTO.class)).thenReturn(Mono.just(aiStoryResDTO));

        // When: saveStory 메서드를 호출
        StoryResDTO result = storyService.saveStory(storyReqDTO);

        // Then: 결과를 검증
        assertEquals("AI generated story", result.getStory());
        verify(storyRepository, times(2)).save(any(StoryEntity.class));  // 처음 저장할 때와 AI 응답을 받아 저장할 때 두 번 저장됨
    }

    @Test
    void resaveStoryTest() {
        // Given: 테스트 데이터 설정
        ReStoryReqDTO reStoryReqDTO = new ReStoryReqDTO();
        reStoryReqDTO.setId(1);
        reStoryReqDTO.setStoryIndex(1);

        // StoryEntity 생성
        StoryEntity storyEntity = new StoryEntity();
        storyEntity.setId(1);  // setter 사용
        storyEntity.setStory("Original story");

        // AI 서버에서 응답할 데이터를 설정
        AiStoryResDTO aiStoryResDTO = new AiStoryResDTO();
        aiStoryResDTO.setId(1);
        aiStoryResDTO.setStory("New AI generated story");
        aiStoryResDTO.setStoryIndex(1);

        // StoryRepository와 WebClient의 동작을 Mock으로 설정
        when(storyRepository.findById(1)).thenReturn(java.util.Optional.of(storyEntity));
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(AiStoryResDTO.class)).thenReturn(Mono.just(aiStoryResDTO));

        // When: resaveStory 메서드를 호출
        StoryResDTO result = storyService.resaveStory(reStoryReqDTO);

        // Then: 결과 검증
        assertEquals("New AI generated story", result.getStory());
        verify(storyRepository, times(1)).save(any(StoryEntity.class));  // 한번 저장됨
    }
}