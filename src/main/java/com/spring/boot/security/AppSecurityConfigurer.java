package com.spring.boot.security;

import com.spring.boot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 自定义Spring Security认证处理类
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 创建密码的加密程序
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        // 设置UserDetailsService
        auth.userDetailsService(userService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }

    @Autowired
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /*@Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 由于使用的是JWT，我们这里不需要csrf
                //.csrf().disable()
                // 基于token，所以不需要session
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .authorizeRequests()
                //Spring Security 5.0 之后需要过滤静态资源
//                .antMatchers("/", "/templates/*.html","/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js").permitAll()
                .antMatchers("/login", "/css/**", "/favicon.ico").permitAll()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/auth/**").permitAll()
                //只能admin角色访问
                .antMatchers("/admin/**").hasRole("ADMIN")
                //ADMIN和DBA的角色可以访问
                .antMatchers("/file/**").hasAnyRole("ADMIN", "DBA")
                //所有接口，登录后才能访问

                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //禁用缓存
        httpSecurity.headers().cacheControl();
        //super.configure(http);

//                .and()
        //登录页面，登录成功后执行的方法
//                .formLogin().loginPage("/login")//.successHandler(appAuthenticationSuccessHandler)
        //用户名和密码
        //.usernameParameter("loginName").passwordParameter("password")
        //指定登录成功后转向的页面
        //.defaultSuccessUrl("/success")
        //指定登录失败后转向的页面和传递的参数
//                .failureUrl("//login?error").permitAll()
//                .and()
        //所有用户的可以登出
//                .logout().permitAll()
//                .and()
        //指定异常处理页面
//                .exceptionHandling().accessDeniedPage("/accessDenied");
    }


    //依赖注入用户认证接口
//    @Autowired
//    private AuthenticationProvider authenticationProvider;

    //依赖注入认证处理成功类，验证用户成功后处理不同用户跳转到不同的页面
//    @Autowired
//    private AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;


    /**
     * DaoAuthenticationProvider 是Spring Security提供的AuthenticationProvider实现
     *
     * @return
     */
//    @Bean
    /*public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //不要隐藏“用户未找到”异常
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        //添加自定义的认证方式
        daoAuthenticationProvider.setUserDetailsService(userService);
        //设置密码加密程序认证
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }*/


}
