package com.oligei.timemanagement.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oligei.timemanagement.utils.TokenUtil;
import com.oligei.timemanagement.utils.msgutils.Msg;
import com.oligei.timemanagement.utils.msgutils.MsgCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
//        System.out.println(request.getRequestURL());
//        String token = request.getHeader("token");
//        System.out.println(token);
//        if (token != null && !token.equals("null")) {
//            boolean result = TokenUtil.authenverify(token);
//            System.out.println(result);
//            if (result) {
//                System.out.println("authentication passed");
//                return true;
//            }
//        }
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        try {
//            Msg<Boolean> msg = new Msg<>(MsgCode.NOT_AUTHENTICATED);
//            JSONObject json = (JSONObject) JSON.toJSON(msg);
//            response.getWriter().append(json.toJSONString());
//            System.out.println("authentication failure");
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.sendError(500);
//            return false;
//        }
//        return false;
    }
}
