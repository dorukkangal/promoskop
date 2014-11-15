package com.mudo.promoskop.request;

import java.io.Serializable;

public class FeedbackRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	private String email;

	private String feedback;

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
