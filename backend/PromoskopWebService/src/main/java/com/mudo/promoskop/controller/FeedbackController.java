package com.mudo.promoskop.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mudo.promoskop.request.FeedbackRequest;
import com.mudo.promoskop.util.MailUtil;

@RestController
@RequestMapping(value = "/feedback")
public class FeedbackController {

	@RequestMapping(method = RequestMethod.POST, produces = { "application/json; charset=UTF-8" }, consumes = { "application/json; charset=UTF-8" })
	public @ResponseBody
	void receiveFeedback(@RequestBody FeedbackRequest holder) throws Exception {
		MailUtil.sendGroupMail(MailUtil.EMAIL_GROUP_LIST, holder.getEmail(), holder.getFeedback());
	}
}
