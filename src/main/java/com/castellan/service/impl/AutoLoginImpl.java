package com.castellan.service.impl;

import com.castellan.dao.UserMapper;
import com.castellan.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iAutoLogin")
public class AutoLoginImpl {

    @Autowired
    private UserMapper userMapper;

    public User getUserByToken(String token){
        User user = userMapper.selectUserByToken(token);
        return user;
    }
}
