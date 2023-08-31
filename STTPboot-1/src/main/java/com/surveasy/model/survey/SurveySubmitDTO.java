package com.surveasy.model.survey;


import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveySubmitDTO {
	private int surveyno;
    private int questionno;
    private String type;
    private Map<String, String> answerMap;
}
