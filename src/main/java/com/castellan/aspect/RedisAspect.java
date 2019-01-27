package com.castellan.aspect;


import com.castellan.common.ServerResponse;
import com.castellan.util.JsonUtil;
import com.castellan.util.MD5Util;
import com.castellan.util.RedisPoolUtil;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class RedisAspect {


    @Pointcut("execution(* com.castellan.service.impl.ProductServiceImpl.* (..))")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object arround(ProceedingJoinPoint proceedingJoinPoint){

        Object object = null;

        // 从redis中获取缓存
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        StringBuilder paraInfo = new StringBuilder();
        Object[] paras = proceedingJoinPoint.getArgs();
        for(Object item: paras){
            try {
                paraInfo.append(item.toString());
            } catch (NullPointerException e) {
                paraInfo.append("null");
            }
        }
        String key = MD5Util.getMD5(className + ":" + methodName + ":" + paraInfo);
        String value = RedisPoolUtil.get(key);
        if (value == null){
            try {
                object = proceedingJoinPoint.proceed();
                value = JsonUtil.obj2String(object);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            object = JsonUtil.string2Obj(value, ServerResponse.class);
        }

        // 如果是增删改方法，清空缓存，如果不是设置缓存

        if (methodName.equals("saveOrUpdateProduct") || methodName.equals("setSaleStatus")){
            RedisPoolUtil.flush();
        } else {
            RedisPoolUtil.set(key, value);
        }
        return object;
    }


}
