package com.wifosell.zeus.controller;

import com.wifosell.zeus.model.mail.Mail;
import com.wifosell.zeus.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/sendmail")
public class MailController {
    private final MailService mailService;

    @Autowired
    private ApplicationContext _applicationContext;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
    @GetMapping("/all")
    public String sendMailCustomers() {
        Mail mail = new Mail();
        mail.setMailFrom("ductm@evscare.com");
        mail.setMailTo("enpro1955@gmail.com");
        mail.setMailSubject("Spring Boot - Email Example");
        mail.setMailContent("Learn How to send Email using Spring Boot!!!");
        MailService mailService = (MailService) _applicationContext.getBean("mailService");
        mailService.sendEmail(mail);
        return "SUCCESS";
    }
}
