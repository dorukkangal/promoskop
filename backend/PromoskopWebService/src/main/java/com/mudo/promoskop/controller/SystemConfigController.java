package com.mudo.promoskop.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mudo.promoskop.service.JsonService;
import com.mudo.promoskop.util.PropertyUtil;

@Controller
@RequestMapping(value = "/")
public class SystemConfigController {

	@Autowired
	private JsonService jsonService;

	@RequestMapping(value = "/config", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	String getConfig() throws Exception {
		HashMap<String, Object> properties = PropertyUtil.getSystemConfigProperties();
		return jsonService.generateJsonForAppConfiguration(properties);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String checkSystemHealth() {
		return "index";
	}
}