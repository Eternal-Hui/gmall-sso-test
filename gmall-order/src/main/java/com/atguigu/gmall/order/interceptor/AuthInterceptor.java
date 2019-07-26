package com.atguigu.gmall.order.interceptor;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.order.anotation.LoginRequired;
import com.atguigu.gmall.order.util.CookieUtil;
import com.atguigu.gmall.order.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod hm = (HandlerMethod) handler;

        Annotation methodAnnotation = hm.getMethodAnnotation(LoginRequired.class);

        // 检查该方法是否需要做登录检查
        if (methodAnnotation == null){
            return true;
        }

        String token = "";

        // 获取缓存中token
        String oldToken = CookieUtil.getCookieValue(request,"cookieToken",true);
        if (StringUtils.isNotBlank(oldToken)){
            token = oldToken;
        }
        // 获取请求地址中token
        String newToken = request.getParameter("newToken");
        if (StringUtils.isNotBlank(newToken)){
            token = newToken;
        }

        // 获取业务方法的地址
        String requestURL = request.getRequestURL().toString();

        // 调用方法验证token的真实性
        String isSuccess = HttpclientUtil.doGet("http://localhost:8082/verify?token=" + token);
        Map map = JSON.parseObject(isSuccess, Map.class);
        String result = (String) map.get("result");
        if (StringUtils.isNotBlank(token)){
            if (!result.equals("success")){
                // token无效,重定向到登录页面并带上业务请求地址
                response.sendRedirect("http://localhost:8082/loginIndex?returnUrl=" + requestURL);
                return false;
            }
        }else {
            // token为null,重定向到登录页面并带上业务请求地址
            response.sendRedirect("http://localhost:8082/loginIndex?returnUrl=" + requestURL);
            return false;
        }

        CookieUtil.setCookie(request,response,"cookieToken",token,60*60,true);

        return true;
    }
}
