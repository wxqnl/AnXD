package com.cug.daijiaguanli.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String KEY = "rem";

    // 接收业务数据,生成token并返回
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12)) // 设置过期时间为12小时
                .sign(Algorithm.HMAC256(KEY));
    }

    // 接收token, 进行校验, 并返回业务数据
    public static Map<String, Object> parseToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(KEY)).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("claims").asMap();
    }
}
