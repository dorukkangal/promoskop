package com.mudo.promoskop.controller;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mudo.promoskop.request.FeedbackRequest;
import com.mudo.promoskop.service.JsonService;


@RestController
@RequestMapping(value = "/feedback")
public class FeedbackController {

	@Autowired
	JsonService jsonService;
	
	private static Logger LOG = LoggerFactory.getLogger(FeedbackController.class);
	
	@RequestMapping(value="/mustafa",method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" }, consumes = { "application/json; charset=UTF-8" })
	public @ResponseBody
	String receiveFeedback(@RequestBody FeedbackRequest holder) throws Exception {
		System.out.println("receiveFeedback geldi");
		LOG.debug("******Email : " + holder.getEmail() + "\n Feedback : " + holder.getFeedback() + "*********") ;
		return jsonService.generateJsonForFeedback(holder.getEmail(), holder.getFeedback());
	}
}
