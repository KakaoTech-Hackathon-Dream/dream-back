package com.example.dream_back.program.controller;

import com.example.dream_back.program.dto.ProgramReqDTO;
import com.example.dream_back.program.dto.ProgramResDTOs;
import com.example.dream_back.program.service.ProgramService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ProgramController {

    private final ProgramService programService;

    @PostMapping("/program")
    public ProgramResDTOs getProgram(@RequestBody ProgramReqDTO programReqDTO){

        return programService.getReferralPrograms(programReqDTO);
    }
}
