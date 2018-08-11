package com.email.config;

import org.apache.commons.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.Messaga;
import java.mail.internet.InternetAddress;

public class EmailService {
 private final JavaMailSender mailSender;
 
 private final String fromEmailAddress;
 
 public EmailService(JavaMailSender mailSender, String fromEmailAddress) {
 this.mailSender = mailSender;
 this.fromEmailAddress = fromEmailAddress;
 }
 
 public void sendMessage(String to, String subject, String text, String contentType) {
 MimeMessagePreparator preparator = getMimeMessagePreparator(to, subject, text, contentType);
 
 try{
 StringBuffer debugText = new StringBuffer();
 
 debugText.append("TYPE: " + contentType + "\n");
 debugText.append("TO: " + to + "\n");
 debugText.append("SUBJECT: " + subject + "\n");
 debugText.append(text);
 
 
 log.debug(debugText.toString());
 
 this.mailSender.send(preparator);
 }
 catch (MailException ex) {
 log.error("Error sending email" + ex.getMessage());
 }
 }
 
 public MimeMessagePreparator getMimeMessagePreparator(String to, String subject, String text, String contentType){
 MimeMessagePreparator preparator = mimeMessage -> {
 mimeMessage.setFrom(new InternetAddress(fromEmailAddress));
 mimeMessage.setContent(StringEscapeUtils.unescapeHtm(textl), contentType);
 mimeMessage.setRecipient(Message.RecipientType.TO, new InterentAddress(to));
 mimeMessage.setSubject(subject);
 mimeMessage.setHeader("Content-Transfer-Encoding", "quoted-printable");
 };
 return preparator;
 }
}