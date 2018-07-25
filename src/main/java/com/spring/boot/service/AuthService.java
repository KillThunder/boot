package com.spring.boot.service;

import com.spring.boot.pojo.User;

public interface AuthService {

    User register(User userToAdd);

    String login(String username, String password);

    String refresh(String oldToken);
}
