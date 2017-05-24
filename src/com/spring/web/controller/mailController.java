package com.spring.web.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller
public class mailController {
	
	@RequestMapping("/")
	public String showHome(){
		
		return "home";
	}
	
	@Autowired
    private JavaMailSender mailSender;
     
    @RequestMapping(value= "/sendEmail.do", method = RequestMethod.POST)
    public String doSendEmail(HttpServletRequest request, final @RequestParam CommonsMultipartFile attachFile) {
        // takes input from e-mail form
        String recipientAddress = request.getParameter("mailTo");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

         
        // prints debug info
        System.out.println("To: " + recipientAddress);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        System.out.println("attachFile: " + attachFile.getOriginalFilename());
         
        mailSender.send(new MimeMessagePreparator() {
        	 
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper messageHelper = new MimeMessageHelper(
                        mimeMessage, true, "UTF-8");
                messageHelper.setTo(recipientAddress);
                messageHelper.setSubject(subject);
                messageHelper.setText(message);
                 
                // determines if there is an upload file, attach it to the e-mail
                String attachName = attachFile.getOriginalFilename();
                if (!attachFile.equals("")) {
 
                    messageHelper.addAttachment(attachName, new InputStreamSource() {
                         
                        @Override
                        public InputStream getInputStream() throws IOException {
                            return attachFile.getInputStream();
                        }
                    });
                }
                 
            }
 
        });
         
        // forwards to the view named "Result"
        return "Result";
    }
	
}
