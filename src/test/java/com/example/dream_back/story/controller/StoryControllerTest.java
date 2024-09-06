package com.example.dream_back.story.controller;

import com.example.dream_back.story.dto.ReStoryReqDTO;
import com.example.dream_back.story.dto.StoryReqDTO;
import com.example.dream_back.story.dto.StoryResDTO;
import com.example.dream_back.story.service.StoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = StoryController.class)
class StoryControllerTest {

    @MockBean
    private StoryService storyService;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        // WebTestClient 설정: StoryController를 주입하여 클라이언트 구성
        webTestClient = WebTestClient.bindToController(new StoryController(storyService)).build();
    }

    @Test
    void createStoryTest() {
        // Given: 테스트 데이터 설정
        StoryReqDTO storyReqDTO = new StoryReqDTO();
        storyReqDTO.setJob("Engineer");
        storyReqDTO.setText("Sample text");

        StoryResDTO storyResDTO = new StoryResDTO();
        storyResDTO.setId(1);
        storyResDTO.setStory("AI generated story");

        // StoryService의 saveStory 메서드 동작을 Mock으로 설정
        when(storyService.saveStory(any(StoryReqDTO.class))).thenReturn(storyResDTO);

        // When & Then: WebTestClient를 통해 POST 요청을 보내고 응답 검증
        webTestClient.post().uri("/api/story")
                .bodyValue(storyReqDTO)  // 요청 본문 설정
                .exchange()  // 요청 실행
                .expectStatus().isOk()  // 응답 상태가 200 OK인지 확인
                .expectBody(StoryResDTO.class)  // 응답 본문을 StoryResDTO로 매핑하여 확인
                .value(resDTO -> resDTO.getStory().equals("AI generated story"));
    }

    @Test
    void recreateStoryTest() {
        // Given: 테스트 데이터 설정
        ReStoryReqDTO reStoryReqDTO = new ReStoryReqDTO();
        reStoryReqDTO.setId(1);
        reStoryReqDTO.setStoryIndex(1);

        StoryResDTO storyResDTO = new StoryResDTO();
        storyResDTO.setId(1);
        storyResDTO.setStory("AI updated story");

        // StoryService의 resaveStory 메서드 동작을 Mock으로 설정
        when(storyService.resaveStory(any(ReStoryReqDTO.class))).thenReturn(storyResDTO);

        // When & Then: WebTestClient를 통해 POST 요청을 보내고 응답 검증
        webTestClient.post().uri("/api/story/re")
                .bodyValue(reStoryReqDTO)  // 요청 본문 설정
                .exchange()  // 요청 실행
                .expectStatus().isOk()  // 응답 상태가 200 OK인지 확인
                .expectBody(StoryResDTO.class)  // 응답 본문을 StoryResDTO로 매핑하여 확인
                .value(resDTO -> resDTO.getStory().equals("AI updated story"));
    }
}