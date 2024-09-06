package com.example.dream_back.program.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProgramResDTOs {
    private String status;
    private List<ProgramResDTO> referral_programs;
}
