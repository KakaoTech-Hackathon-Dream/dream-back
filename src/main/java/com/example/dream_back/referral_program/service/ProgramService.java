package com.example.dream_back.referral_program.service;


import com.example.dream_back.referral_program.dto.AiProgramDTO;
import com.example.dream_back.referral_program.dto.ProgramReqDTO;
import com.example.dream_back.referral_program.dto.ProgramResDTO;
import com.example.dream_back.referral_program.dto.ProgramResDTOs;
import com.example.dream_back.story.dto.AiStoryResDTO;
import com.example.dream_back.story.entity.StoryEntity;
import com.example.dream_back.story.repositoty.StoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProgramService {

    private final WebClient webClient;

    private static final String AI_URI = "http://ai:8000";

    private StoryRepository storyRepository;

    public ProgramService(StoryRepository storyRepository, WebClient.Builder webClientBuilder) {
        this.storyRepository = storyRepository;
        this.webClient = webClientBuilder.build();
    }

    public ProgramResDTOs getReferralPrograms(ProgramReqDTO programReqDTO){
        StoryEntity storyEntity = storyRepository.findById(programReqDTO.getStoryIndex())
                .orElseThrow(() -> new RuntimeException("Story not found with id: " + programReqDTO.getStoryIndex()));

        return webClient.post()
                .uri(AI_URI + "/ai/program")
                .bodyValue(new AiProgramDTO(storyEntity.getStory()))
                .retrieve()
                .bodyToMono(ProgramResDTOs.class)
                .block();
    }
}
