package com.survey.sttp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class Test {
	@RequestMapping(value="/test", method = RequestMethod.GET)
		return "index";
	}
}	
