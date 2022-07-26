package com.winemarketv2.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	MailProperties mailProperties;

	List<MimeMessage> mimeMessages = new ArrayList<MimeMessage>();

	public void sendMail(String to, String subject, String templateName, Context context)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage mail = javaMailSender.createMimeMessage();
		String body = templateEngine.process(templateName, context);
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setFrom("winemarket.247@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(body, true);

		mimeMessages.add(mail);
	}

	@Scheduled(fixedDelay = 2000)
	public void run() {
		while (!mimeMessages.isEmpty()) {
			MimeMessage message = mimeMessages.remove(0);
			try {
				javaMailSender.send(message);
			} catch (Exception e) {
			}
		}
	}

}