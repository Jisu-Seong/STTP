package com.surveasy.form.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Survey {
	private String surveytitle;
    private String surveycontent;
    private boolean is_public_survey;
    private boolean require_login;
    private boolean is_public_result;
    private boolean show_progress;
    private LocalDateTime currentTime;
    private List<Question> questions;
}
