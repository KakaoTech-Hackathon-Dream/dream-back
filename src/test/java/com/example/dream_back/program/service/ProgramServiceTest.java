package com.example.dream_back.program.service;

import com.example.dream_back.program.dto.ProgramReqDTO;
import com.example.dream_back.program.dto.ProgramResDTOs;
import com.example.dream_back.story.entity.StoryEntity;
import com.example.dream_back.story.repositoty.StoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgramServiceTest {

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
    private ProgramService programService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getReferralPrograms_Success() {
        // Given
        ProgramReqDTO programReqDTO = new ProgramReqDTO();
        programReqDTO.setStoryIndex(1);
        StoryEntity storyEntity = new StoryEntity();
        storyEntity.setStory("Sample Story");

        ProgramResDTOs programResDTOs = new ProgramResDTOs(); // Assuming it has a default constructor

        when(storyRepository.findById(1)).thenReturn(Optional.of(storyEntity));
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ProgramResDTOs.class)).thenReturn(Mono.just(programResDTOs));

        // When
        ProgramResDTOs result = programService.getReferralPrograms(programReqDTO);

        // Then
        assertNotNull(result);
        verify(storyRepository).findById(1);
        verify(webClient).post();
    }

    @Test
    public void getReferralPrograms_StoryNotFound() {
        // Given
        ProgramReqDTO programReqDTO = new ProgramReqDTO();
        programReqDTO.setStoryIndex(1);

        when(storyRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> programService.getReferralPrograms(programReqDTO));
        assertEquals("Story not found with id: 1", exception.getMessage());
        verify(storyRepository).findById(1);
    }

    @Test
    public void getReferralPrograms_FailOnWebClient() {
        // Given
        ProgramReqDTO programReqDTO = new ProgramReqDTO();
        programReqDTO.setStoryIndex(1);
        StoryEntity storyEntity = new StoryEntity();
        storyEntity.setStory("Sample Story");

        when(storyRepository.findById(1)).thenReturn(Optional.of(storyEntity));
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ProgramResDTOs.class)).thenReturn(Mono.error(WebClientResponseException.BadRequest.create(400, "Bad Request", null, null, null)));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> programService.getReferralPrograms(programReqDTO));
        assertTrue(exception.getMessage().contains("400 Bad Request"));
    }
}