package com.wifosell.zeus.service;

import com.wifosell.zeus.model.user.User;
import com.wifosell.zeus.payload.request.RegisterRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public interface AuthService {
    User register(RegisterRequest registerRequest) ;

}
