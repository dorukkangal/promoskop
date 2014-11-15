package com.mudo.promoskop.util;

import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {
	public static final String[] EMAIL_GROUP_LIST = new String[] { "dorukkangal@gmail.com"};//, "mustafabesnili@hotmail.com", "ceyhunozugur@gmail.com" };
	private static final String USER_NAME = "infopromoskop@gmail.com";
	private static final String PASSWORD = "gsustartup0493;";
	private static final String SUBJECT = "Promoskop Feedback";
	private static final String BODY = "Email : {0}<br>Feedback : {1}"; 

	public static void sendMail(String to, String email, String feedback) throws Exception {

		MimeMessage message = new MimeMessage(openSession(USER_NAME, PASSWORD));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setFrom(new InternetAddress(USER_NAME));
		message.setSubject(SUBJECT, "UTF-8");
		message.setContent(MessageFormat.format(BODY, email, feedback), "text/html; charset=utf-8");
		Transport.send(message);
	}

	public static void sendGroupMail(String[] addresses, String email, String feedback) throws Exception {

		MimeMessage message = new MimeMessage(openSession(USER_NAME, PASSWORD));
		for (String address : addresses) {
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress(address));
		}
		message.setSubject(SUBJECT, "UTF-8");
		message.setContent(MessageFormat.format(BODY, email, feedback), "text/html; charset=utf-8");

		Transport.send(message);
	}

	private static Session openSession(final String from, final String password) {
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		return Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});
	}
}
