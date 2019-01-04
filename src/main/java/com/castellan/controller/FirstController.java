package com.castellan.controller;

import com.castellan.dao.UserMapper;
import com.castellan.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {

    @Autowired
    UserMapper userMapper;

    @RequestMapping("/user/{id}")
    public User getUserById(@PathVariable Integer id){

        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

}
