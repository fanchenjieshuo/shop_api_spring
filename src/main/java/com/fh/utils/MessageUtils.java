package com.fh.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

//发送短信
public class MessageUtils {

 public static String sendMessage(String mobile){

  String newcode = Integer.toString((int) (Math.random()*9000+1000));  //每次调用生成一位四位数的随机数
  DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4GBakurzGbzfWfjEETcz", "nYm3UKuZhVgxYd6bKZWPO6dw9bugqu");
  IAcsClient client = new DefaultAcsClient(profile);

  CommonRequest request = new CommonRequest();
  request.setSysMethod(MethodType.POST);
  request.setSysDomain("dysmsapi.aliyuncs.com");
  request.setSysVersion("2017-05-25");
  request.setSysAction("SendSms");
  request.putQueryParameter("RegionId", "cn-hangzhou");
  request.putQueryParameter("PhoneNumbers", mobile);
  request.putQueryParameter("SignName", "abc商城");
  request.putQueryParameter("TemplateCode", "SMS_197610118");
  request.putQueryParameter("TemplateParam", "{'code':"+newcode+"}");
  try {
   CommonResponse response = client.getCommonResponse(request);

  } catch (Exception e) {
   e.printStackTrace();
  }

  return newcode;
 }

}
