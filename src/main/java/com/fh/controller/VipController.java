package com.fh.controller;

import com.fh.common.JsonData;
import com.fh.utils.JWT;
import com.fh.utils.RedisUse;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.HashMap;

@RestController
@RequestMapping("vipController")
public class VipController {


    @RequestMapping("sendMessage")
    @ApiOperation("获取验证码方法")
    public JsonData sendMessage(String iphone){

        String code="1234";
        RedisUse.set(iphone+"vip_lbw",code,60*5);



        return JsonData.getJsonSuccess("发送短信成功");
    }

    @RequestMapping("login")
    @ApiOperation("登录方法")
    public JsonData login(String iphone, String code, HttpServletRequest request){

        HashMap<Object, Object> result = new HashMap<>();
        String s = RedisUse.get(iphone + "vip_lbw");
        if (s!=null && s.equals(code)){
            HashMap<Object, Object> user = new HashMap<>();
            user.put("iphone",iphone);
            String sign = JWT.sign(user, 1000 * 60 * 60 * 24);
            String token= Base64.getEncoder().encodeToString((iphone+','+sign).getBytes());
            RedisUse.set("token_"+iphone,sign,60*30);

            result.put("status","200");
            result.put("message","登录成功");
            result.put("token",token);

        }else {
            result.put("status","300");
            result.put("message","验证码输入错误");

        }


        return JsonData.getJsonSuccess(result);
    }
}
