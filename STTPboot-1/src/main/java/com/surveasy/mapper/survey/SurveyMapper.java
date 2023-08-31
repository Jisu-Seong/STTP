package com.surveasy.mapper.survey;


import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.surveasy.model.survey.Answers;
import com.surveasy.model.survey.SurveyOption;
import com.surveasy.model.survey.SurveyPaper;
import com.surveasy.model.survey.SurveyQuestion;
import com.surveasy.model.survey.SurveyRequire;
import com.surveasy.model.survey.UserSurvey;

@Mapper
public interface SurveyMapper {
	@Select("SELECT * FROM surveypaper WHERE surveyno=#{surveyno}")
	SurveyPaper getSurvey(@Param("surveyno") int surveyno);
	
	@Select("SELECT * FROM survey_option WHERE surveyno=#{surveyno}")
	SurveyOption getSurveyOption(@Param("surveyno") int surveyno);
	
	@Select("SELECT * FROM surveyrequire WHERE surveyno=#{surveyno}")
	SurveyRequire getSurveyRequire(@Param("surveyno") int surveyno);
	
	@Select("SELECT * FROM question WHERE surveyno=#{surveyno}")
	List<SurveyQuestion> getQuestion(@Param("surveyno") int surveyno);
	
	@Select("SELECT * FROM answers WHERE questionno=#{questionno}")
	Answers getAnswer(@Param("questionno") int questionno);
	
	@Insert({"<script>",
		    "INSERT INTO user_survey (userno, surveyno) VALUES (#{userno}, #{surveyno})",
		    "</script>"})
	@Options(useGeneratedKeys = true, keyProperty = "user_survey_no")
	int insertUserSurvey(UserSurvey userSurvey);
}
