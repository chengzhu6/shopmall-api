package com.castellan.common;

import com.castellan.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView resloveGlobalException(HttpServletRequest request, HttpServletResponse response, Exception e){
        log.error("{} : exception {}", request.getRequestURI() ,e);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        ServerResponse serverResponse = ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),"出现异常，请查看后台信息");
        String serverResponseJson = JsonUtil.obj2String(serverResponse);
        out.write(serverResponseJson);
        out.close();
        return null;
    }
}
