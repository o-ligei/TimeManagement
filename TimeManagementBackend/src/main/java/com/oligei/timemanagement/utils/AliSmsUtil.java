package com.oligei.timemanagement.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class AliSmsUtil {

    private static final String product = "Dysmsapi";
    private static final String domain = "dysmsapi.aliyuncs.com";

    private static final String accessKeyId = "LTAI4G3C3cjjsFabv1Vgcscu";
    private static final String accessKeySecret = "vCNGDwdL95rUtAncfz8gkqH9ipH5sl";

    public static void sendALiSms(String phone, String code) throws Exception{
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "OligeiWeb");
        request.putQueryParameter("TemplateCode", "SMS_204125751");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        CommonResponse response = client.getCommonResponse(request);
        System.out.println(response.getData());
    }
}
