package com.castellan.service;

import com.castellan.common.ServerResponse;
import com.castellan.pojo.User;

public interface IUserService {


    ServerResponse login(String username, String password);

    ServerResponse register(User user);

    ServerResponse checkVaild(String str, String type);

    ServerResponse<String> getQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question,String answer);

    ServerResponse resetPassword(String username, String passwordNew, String token);

    ServerResponse resetPassword(String passwordOld, String passwordNew,User user);

    ServerResponse updateInfomation(User user);

    ServerResponse<User> getInfomation(Integer id);

    ServerResponse<User> getUsers(int pageSize, int pageNum);

}
