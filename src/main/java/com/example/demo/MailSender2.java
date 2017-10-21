package com.example.demo;

import com.example.demo.config.ServerConfig;
import com.example.demo.model.DataOwner;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;


@Service
public class MailSender2 {
    
    
    private JavaMailSender mailSender;

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    private SpringTemplateEngine templateEngine;
    
    @Async
    public void sendMail(String from, String to, String subject, String msg) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setSubject(subject);
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setText(msg, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
    
    public void sendMail(DataOwner owner){

        String composedMail = composeEmail(owner);
        ApplicationContext context =
                new ClassPathXmlApplicationContext("Spring-Mail.xml");

        MailSender2 mm = (MailSender2) context.getBean("mailMail");
        mm.sendMail(ServerConfig.EMAIL,
                owner.getEmail(), ServerConfig.EMAIL_TITLE,composedMail);

    }
    
       public String composeEmail(DataOwner owner) {
        
        
        String link ="https://loginmobileapp.herokuapp.com/api/users/confirm/"+owner.getId();
      
        Context ctx = new Context(Locale.ENGLISH);
        ctx.setVariable("username",  owner.getUsername());
        ctx.setVariable("resetLink", link);

        String htmlContent = templateEngine.process("email_template.html", ctx);
        
        return htmlContent;
        
//       return  "<p>Hello   "+ owner.getUsername()+ ",<br /> <br /> " +
//                "You recently opened an account with LoginApp, <br /> please click <a href=\"https://loginmobileapp.herokuapp.com/api/users/confirm/"+owner.getId()
//               +"\">here</a> to confirm your account. <p>";
    }
}
