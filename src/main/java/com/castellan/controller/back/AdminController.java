package com.castellan.controller.back;


import com.castellan.common.Const;
import com.castellan.common.ServerResponse;
import com.castellan.pojo.User;
import com.castellan.service.IUserService;
import com.castellan.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/back/admin/")
public class AdminController {

    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse login(String username, String password, HttpSession session, HttpServletResponse response){
        ServerResponse<User> serverResponse = iUserService.login(username,password);
        if(serverResponse.isSuccess()){
            if (serverResponse.getData().getRole().equals(Const.Role.ROLE_ADMIN)){
                String token = MD5Util.getMD5(username + password);
                Cookie cookie = new Cookie("token",token);
                cookie.setPath("/");
                cookie.setHttpOnly(true);
                cookie.setMaxAge(Const.CookieUtil.MAX_AGE);
                response.addCookie(cookie);
                session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
            } else {
                return ServerResponse.createByErrorMessage("不是管理员，无法登录");
            }

        }
        return serverResponse;

    }

    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getUserList(HttpSession session, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize, @RequestParam(value = "pageNum",defaultValue = "1")int pageNum){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        return iUserService.getUsers(pageSize,pageNum);


    }
}
