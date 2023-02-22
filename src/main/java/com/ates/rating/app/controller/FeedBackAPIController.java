package com.ates.rating.app.controller;

import com.ates.rating.app.GenericResponse;
import com.ates.rating.app.security.security.services.UserDetailsImpl;
import com.ates.rating.app.service.QuestionAnswerService;
import com.ates.rating.app.viewmodel.OptionVM;
import com.ates.rating.app.viewmodel.QuestionAndAnswerVM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Slf4j
public class FeedBackAPIController {
    private final QuestionAnswerService questionAnswerService;
    private final UserDetailsService userService;

    @GetMapping
    public GenericResponse<QuestionAndAnswerVM> getQuestionsAndAnswer(@RequestParam String feedbackType,
                                                                      Principal principal) {
        var user = (UserDetailsImpl) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        var data = questionAnswerService.getAllQuestionAndAnswer(user.getId(), feedbackType);
        return GenericResponse.<QuestionAndAnswerVM>builder()
                .data(data)
                .success(true)
                .build();
    }

    @PostMapping("/save")
    public GenericResponse<Boolean> saveFeedback(String userType, @RequestBody OptionVM optionVM,
                                                 Principal principal) {
        var user = (UserDetailsImpl) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        var data = questionAnswerService.saveAnswer(user, optionVM);
        return GenericResponse.<Boolean>builder()
                .success(true)
                .message(data ? "You have submitted successfully" : "Something went wrong ,please try after sometime")
                .data(data)
                .build();
    }

    @GetMapping("/check-already-submit")
    public GenericResponse<Boolean> checkAlreadySubmit(@RequestParam String userType,
                                                       @RequestParam Long year,
                                                       Principal principal) {
        var user = (UserDetailsImpl) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        var data = questionAnswerService.checkAlreadySubmit(user, userType,year);
        return GenericResponse.<Boolean>builder()
                .success(data)
                .data(data)
                .message(data ? "You have already submitted your response,Thanks for participating" : "")
                .build();
    }


}

