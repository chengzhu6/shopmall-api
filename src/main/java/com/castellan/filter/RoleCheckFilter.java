package com.castellan.filter;

import com.castellan.common.Const;
import com.castellan.common.ResponseCode;
import com.castellan.common.ServerResponse;
import com.castellan.pojo.User;
import com.castellan.service.IUserService;
import com.castellan.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class RoleCheckFilter implements HandlerInterceptor {

    @Autowired
    private IUserService iUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (cookies != null && cookies.length > 0){
            for (Cookie cookie:cookies ) {
                if (Const.CookieUtil.TOKEN.equals(cookie.getName())){
                    // 进行数据库的访问判断是否登录
                    User user = iUserService.getUserByToken(cookie.getValue());
                    if (user != null){
                        // 判断权限，是否为管理员。
                        ServerResponse serverResponse = iUserService.checkAdminRole(user.getRole());
                        if (!serverResponse.isSuccess()){
                            serverResponse = ServerResponse.createByErrorMessage("无权限查操作");
                            String serverResponseJson = JsonUtil.obj2String(serverResponse);
                            out.write(serverResponseJson);
                            out.close();
                            return false;
                        }
                        request.getSession().setAttribute(Const.CURRENT_USER,user);
                        return true;
                    }
                }
            }
        }
        ServerResponse serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"当前用户未登录，请登录");
        String serverResponseJson = JsonUtil.obj2String(serverResponse);
        out.write(serverResponseJson);
        out.close();
        return false;
    }
}
