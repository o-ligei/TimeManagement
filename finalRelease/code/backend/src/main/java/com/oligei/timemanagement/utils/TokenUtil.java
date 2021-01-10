package com.oligei.timemanagement.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oligei.timemanagement.entity.User;

import java.util.Date;

public class TokenUtil {

    private static final long EXPIRE_TIME = 60 * 60 * 1000;
    private static final String TOKEN_SECRET = "oligei!2020";  //密钥盐

    public static String sign(User user) {
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                       .withIssuer("auth0")
                       .withClaim("username", user.getUsername())
                       .withClaim("type", user.getType())
                       .withExpiresAt(expiresAt)
                       .sign(Algorithm.HMAC256(TOKEN_SECRET));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    public static boolean authenverify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过：");
            System.out.println("issuer: " + jwt.getIssuer());
            System.out.println("username: " + jwt.getClaim("username").asString());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean adminverify(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            String type = jwt.getClaim("type").asString();
            if (!type.equals("Admin")) { return false; }
            System.out.println("认证通过：");
            System.out.println("issuer: " + jwt.getIssuer());
            System.out.println("username: " + jwt.getClaim("username").asString());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
