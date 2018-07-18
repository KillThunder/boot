package com.spring.boot.security;

import com.spring.boot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义Spring Security认证处理类
 */
@Configuration
public class AppSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;

    //依赖注入加密接口
    @Autowired
    private PasswordEncoder passwordEncoder;

    //依赖注入用户认证接口
    @Autowired
    private AuthenticationProvider authenticationProvider;

    //依赖注入认证处理成功类，验证用户成功后处理不同用户跳转到不同的页面
    @Autowired
    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;
}
