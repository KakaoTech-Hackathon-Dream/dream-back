package com.example.dream_back.referral_program.controller;

import com.example.dream_back.referral_program.dto.ProgramReqDTO;
import com.example.dream_back.referral_program.dto.ProgramResDTOs;
import com.example.dream_back.referral_program.service.ProgramService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
@AllArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @RequestMapping("/program")
    public ProgramResDTOs getProgram(@RequestBody ProgramReqDTO programReqDTO){

        return programService.getReferralPrograms(programReqDTO);
    }
}
