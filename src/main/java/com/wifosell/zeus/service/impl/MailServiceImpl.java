package com.wifosell.zeus.service.impl;

import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.repository.UserRepository;
import com.wifosell.zeus.service.MailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;

@Service("MailService")
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    public void sendEmail(@NonNull @Email String mailTo, @NonNull String subject, @NonNull String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(new InternetAddress("ductm@evscare.com"));
            mimeMessageHelper.setTo(mailTo);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content);

            mailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(@NonNull Long userId, @NonNull String subject, @NonNull String content) {
        User user = userRepository.getUserById(userId);
        this.sendEmail(user.getEmail(), subject, content);
    }
}
