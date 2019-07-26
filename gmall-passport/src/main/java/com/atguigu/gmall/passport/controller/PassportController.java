package com.atguigu.gmall.passport.controller;

import com.atguigu.gmall.passport.bean.User;
import com.atguigu.gmall.passport.util.JwtUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {

    @RequestMapping("loginIndex")
    public String login(String returnUrl, ModelMap modelMap){

        modelMap.put("returnUrl",returnUrl);

        return "loginIndex";
    }

    @RequestMapping("doLogin")
    @ResponseBody
    public String doLogin(User user, HttpServletRequest request){

        // 验证用户登录信息
        if (!(user.getUsername().equals("jack")&&user.getPassword().equals("123"))){
            return null;
        }
        // 登录成功,调用方法颁发token
        Map<String,Object> map = new HashMap<>();
        map.put("username",user.getUsername());
        map.put("id",user.getId());

        String key = "gmall-sso";

        String salt = request.getHeader("x-forward-for");
        if (salt==null || salt.length() == 0){
            String remoteAddr = request.getRemoteAddr();
            if (remoteAddr==null || remoteAddr.length() == 0){
                return null;
            }else {
                salt = remoteAddr;
            }
        }

        String token = JwtUtil.encode(key, map, salt);

        return token;
    }

    @RequestMapping("verify")
    @ResponseBody
    public Map<String,String> verifyToken(String token,HttpServletRequest request){

        Map<String,String> returnMap = new HashMap<>();

        if (token == null || token.length() == 0){
            returnMap.put("result","failed");
            return returnMap;
        }
        // 进行token解码
        String salt = request.getHeader("x-forward-for");
        if (salt==null || salt.length() == 0){
            String remoteAddr = request.getRemoteAddr();
            if (remoteAddr==null || remoteAddr.length() == 0){
                return null;
            }else {
                salt = remoteAddr;
            }
        }
        Map<String, Object> objectMap = JwtUtil.decode(token, "gmall-sso", salt);
        // 验证token
        if (objectMap == null){
            returnMap.put("result","failed");
            return returnMap;
        }
        returnMap.put("result","success");
        return returnMap;
    }


}
