package com.castellan.service;

import com.castellan.pojo.User;

public interface IAutoLogin {

    User getUserByToken(String token);

}
