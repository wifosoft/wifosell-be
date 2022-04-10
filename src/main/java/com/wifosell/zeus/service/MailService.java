package com.wifosell.zeus.service;

import lombok.NonNull;

import javax.validation.constraints.Email;

public interface MailService {
    void sendEmail(@NonNull @Email String mailTo, @NonNull String subject, @NonNull String content);

    void sendEmail(@NonNull Long userId, @NonNull String subject, @NonNull String content);
}
