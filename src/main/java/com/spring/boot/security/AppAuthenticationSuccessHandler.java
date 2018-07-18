package com.spring.boot.security;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * 认证成功处理类
 */
@Component
public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationFailureHandler {

}
