package com.spring.boot.util;

import com.spring.boot.pojo.JwtUser;
import com.spring.boot.pojo.Role;
import com.spring.boot.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(user.getId(), user.getLoginName(), user.getUserName(), user.getPassWord(), mapToGrantedAuthorities(user.getRoleList()), user.getAge());
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roleList) {
        List<String> authorities = new ArrayList<>();
        for (Role role : roleList) {
            authorities.add(role.getAuthority());

        }
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
