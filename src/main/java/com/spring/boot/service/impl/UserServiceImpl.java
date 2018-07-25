package com.spring.boot.service.impl;

import com.spring.boot.pojo.Role;
import com.spring.boot.pojo.User;
import com.spring.boot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 通过该方法查询用户
     *
     * @param username 此处是登录账号
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLoginName(username);
        if (null == user || 0 != user.getState()) {
            throw new UsernameNotFoundException("用户名不存在！");
        }
        //创建集合保存用户的权限，GrantedAuthority对象代表赋予当前用户的权限
        ArrayList<GrantedAuthority> authorityList = new ArrayList<>();
        for (Role role : user.getRoleList()) {
            authorityList.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassWord(), authorityList);
    }
}
