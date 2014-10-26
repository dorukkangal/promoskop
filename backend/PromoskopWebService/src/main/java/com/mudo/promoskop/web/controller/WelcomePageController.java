package com.mudo.promoskop.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class WelcomePageController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getWelcomePage() {
		return "index";
	}
}