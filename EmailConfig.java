package com.email.config;

import com.utiles.XSSValidator;
import com.email.services.EmailServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.content.annotation.Bean;
import org.springframework.content.annotation.Configuration;
import org.springframework.content.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.map;
import java.util.Properties;

@Configuration
public class EmailConfig {
@Bean
public XSSValidator xssValidator(){
return new XSSValidator();
}

@Bean
@Profile("dev")
public JavaMailSender getJavaMailSender(@Value('${spring.mail.host}") String host,
                                        @Value('${spring.mail.properties.mail.transport.protocol}") String Protocol) {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(host);
    
    
    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", protocol);
    
    return mailSender;
    }
    
    @Bean 
    @Profile("!dev") 
    public JavaMailSender getNonDevJavaMailSender(@Value("${spring.mail.host}") String host,
                                                  @Value("${spring.mail.properties.mail.transport.protocol}") String Protocol,
                                                  @Value("${spring.mail.port:0}") Integer port) {
                                                  
                JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
                mailSender.setHost(host);
                mailSender.setPort(port);
                
                Properties props = mailSender.getJavaMailProperties();
                props.put("mail.transport.protocol", protocol);
                
                return mailSender;
                }
                
        @Bean
        @Profile("dev")
        public EmailService emailService(JavaMailSender mailSender,
                                         @Value("${vcap.services}") String vcapServices) {
        Map<String, String> emailMap = extractUserProvidedServiceCredentials(vcapServices, "email");
                return new EmailService(mailSender, emailMap.get("fromEmailAddress"))

                                         }
        @Bean
        @Profile("!dev")
        public EmailService emailService(JavaMailSender mailSender){
                return new EmailService(mailSender, "any email");
                }
        }
                                               
                                               
//    we need to use in any use cases to send email
    public Email getEmailContent(String keyName){
        JsonNode jsonNode = getContent();
        
        JsonNode emailNode = jsonNode.get(COPY_NODE_KEY).get(EMAIL_NODE_KEY);
        JsonNode keyNameNode = emailNode.get(keyName);
        
        Email email = Email.builder()
            .subject(keyNameNode.get("subject").asText())
            .text(keyNameNode.get("text").asText())
            .buils();
        
        return email;
    }
                                               
                                               


