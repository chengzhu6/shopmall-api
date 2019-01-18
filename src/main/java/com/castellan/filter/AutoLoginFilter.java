package com.castellan.filter;

import com.castellan.common.Const;
import com.castellan.common.ServerResponse;
import com.castellan.pojo.User;
import com.castellan.service.IAutoLogin;

import com.castellan.util.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


@Component
public class AutoLoginFilter implements HandlerInterceptor {

    @Autowired
    private IAutoLogin iAutoLogin;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0){
            for (Cookie cookie:cookies ) {
                if (Const.CookieUtil.TOKEN.equals(cookie.getName())){
                    // 进行数据库的访问判断是否登录
                    User user = iAutoLogin.getUserByToken(cookie.getValue());
                    if (user != null){
                        request.getSession().setAttribute(Const.CURRENT_USER,user);
                        return true;
                    }
                }
            }
        }
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        ServerResponse serverResponse = ServerResponse.createByErrorMessage("请登录！");
        String serverResponseJson = JsonUtil.serialize(serverResponse);
        out.write(serverResponseJson);
        return false;
    }

}
