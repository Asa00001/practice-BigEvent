package com.asa;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGen() {
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("username","张三");

        String token = JWT.create()
                .withClaim("user", claims)  //添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()*1000*60*12))  //添加过期时间
                .sign(Algorithm.HMAC256("Asa")); //指定算法，配置密钥

        System.out.println(token);
    }

    @Test
    public void testPass() {
        //定义字符串，模拟用户传递的token
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8oOS4iSJ9LCJleHAiOjEyODQ5ODgwNDgxNjA0MDB9." +
                "ZgVh4ZncsSj1A1mIhatq9rwUzn8-23QDNQVdzNceVCM";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("Asa")).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token); //解析后的JWT对象
        Map<String, Claim> claims = decodedJWT.getClaims();

        System.out.println(claims);
        //如果篡改了头部或载荷位置的数据，验证失败
        //如果密钥更改了，也会验证失败（密钥必须匹配）
        //有效时间过了密钥也会失败
    }
}

