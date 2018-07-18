package com.spring.boot.repository;

import com.spring.boot.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据登录名查询用户
     *
     * @param loginName 登录账号
     * @return
     */
    User findByLoginName(String loginName);
}
