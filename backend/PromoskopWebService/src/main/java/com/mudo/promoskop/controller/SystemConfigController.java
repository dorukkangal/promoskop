package com.mudo.promoskop.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mudo.promoskop.util.PropertyUtil;

@Controller
@RequestMapping(value = "/")
public class SystemConfigController {

	@RequestMapping(value = "/config", method = RequestMethod.GET, produces = { "application/json; charset=UTF-8" })
	public @ResponseBody
	HashMap<String, Object> getConfig() throws Exception {
		HashMap<String, Object> properties = PropertyUtil.getSystemConfigProperties();
		return properties;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String checkSystemHealth() {
		return "index";
	}
}