package com.castellan.controller.back;


import com.castellan.common.Const;
import com.castellan.common.ServerResponse;
import com.castellan.pojo.User;
import com.castellan.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/back/admin/")
public class AdminController {

    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse login(String username, String password, HttpSession session){
        ServerResponse<User> serverResponse = iUserService.login(username,password);
        if(serverResponse.isSuccess()){
            if (serverResponse.getData().getRole().equals(Const.Role.ROLE_ADMIN)){
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
        if(currentUser.getRole().equals(Const.Role.ROLE_CUSTUMER)){
            return ServerResponse.createByErrorMessage("无权限查看");
        }
        return iUserService.getUsers(pageSize,pageNum);


    }
}
