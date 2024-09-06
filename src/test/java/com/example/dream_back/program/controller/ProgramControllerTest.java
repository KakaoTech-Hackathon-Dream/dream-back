package com.example.dream_back.program.controller;

import com.example.dream_back.program.dto.ProgramReqDTO;
import com.example.dream_back.program.dto.ProgramResDTO;
import com.example.dream_back.program.dto.ProgramResDTOs;
import com.example.dream_back.program.service.ProgramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ProgramControllerTest {

    @Mock
    private ProgramService programService;

    @InjectMocks
    private ProgramController programController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(programController).build();
    }

    @Test
    public void getProgram_Success() throws Exception {
        // Given
        ProgramReqDTO programReqDTO = new ProgramReqDTO();
        ProgramResDTOs programResDTOs = new ProgramResDTOs();
        List<ProgramResDTO> list = new ArrayList<>();
        list.add(new ProgramResDTO());
        programReqDTO.setStoryIndex(1);
        programResDTOs.setReferral_programs(list);

        when(programService.getReferralPrograms(programReqDTO)).thenReturn(programResDTOs);

        // When & Then
        mockMvc.perform(post("/api/program")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"storyIndex\": 1}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.referral_programs").exists()); // Change this to actual field if necessary
    }
}