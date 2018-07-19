package com.spring.boot.security;

import com.spring.boot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    /**
     * 创建密码的加密程序
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DaoAuthenticationProvider 是Spring Security提供的AuthenticationProvider实现
     *
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //不要隐藏“用户未找到”异常
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        //添加自定义的认证方式
        daoAuthenticationProvider.setUserDetailsService(userService);
        //设置密码加密程序认证
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //Spring Security 5.0 之后需要过滤静态资源
                .antMatchers("/login", "/css/**", "/js/**", "/img/**").permitAll()
                //只能admin角色访问
                .antMatchers("/admin/**").hasRole("ADMIN")
                //ADMIN和DBA的角色可以访问
                .antMatchers("/file/**").hasAnyRole("ADMIN", "DBA")
                //所有接口，登录后才能访问
                .anyRequest().authenticated()
                .and()
                //登录页面，登录成功后执行的方法
                .formLogin().loginPage("/login").successHandler(appAuthenticationSuccessHandler)
                //用户名和密码
                .usernameParameter("loginName").passwordParameter("password")
                .and()
                //所有用户的可以登出
                .logout().permitAll()
                .and()
                //发生异常跳转的页面
                .exceptionHandling().accessDeniedPage("/accessDenied");
        super.configure(http);
    }
}
