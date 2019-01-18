package com.castellan.controller.pre;


import com.castellan.common.Const;
import com.castellan.common.ResponseCode;
import com.castellan.common.ServerResponse;
import com.castellan.pojo.User;
import com.castellan.service.IUserService;
import com.castellan.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;


    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse response){

        ServerResponse<User> serverResponse = iUserService.login(username,password);
        if(serverResponse.isSuccess()){
            String token = MD5Util.getMD5(serverResponse.getData().getUsername()+serverResponse.getData().getPassword());
            Cookie cookie = new Cookie("token",token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(Const.CookieUtil.MAX_AGE);
            response.addCookie(cookie);
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }


    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse register(User user){

        return iUserService.register(user);
    }

    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse checkValid( String username, String type){
        ServerResponse serverResponse= iUserService.checkVaild(username,type);
        return serverResponse;
    }


    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }


    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo (HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.getQuestion(username);
    }


    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse forgetCheckAnswer(String username, String question, String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse forgetResetPassword(String username, String passwordNew, String token){
        return iUserService.resetPassword(username, passwordNew, token);
    }


    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse resetPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorMessage("请登录");
        }

        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }


    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateInformation(HttpSession session, User user){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录，请先登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse serverResponse = iUserService.updateInfomation(user);
        if (serverResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return serverResponse;
    }

    @RequestMapping(value = "get_infomation.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse getInfomation(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请登录！");
        }
        return iUserService.getInfomation(currentUser.getId());

    }



}
