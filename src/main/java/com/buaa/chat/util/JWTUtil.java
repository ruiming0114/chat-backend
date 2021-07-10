package com.buaa.chat.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    private static final long EXPIRE = 20*60*1000L;

    private static final String SECRET="SpringSalt123";

    public static String createToken(String mongoId,Long current){
        Date date = new Date(current+EXPIRE);
        Map<String,Object> map = new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");
        String token = null;
        try {
            token = JWT.create()
                    .withHeader(map)
                    .withClaim("mongoId",mongoId)
                    .withClaim("current",current)
                    .withExpiresAt(date)
                    .withIssuedAt(new Date())
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (UnsupportedEncodingException e ){
            e.printStackTrace();
        }
        return token;
    }

    public static boolean verify(String token){
        try{
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            verifier.verify(token);
            return true;
        }catch (Exception e ){
            e.printStackTrace();
            return false;
        }
    }

    public static String getMongoId(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("mongoId").asString();
        }catch (JWTDecodeException e){
            return null;
        }
    }

    public static Long getExpire(String token){
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("current").asLong();
        }catch (Exception e){
            return null;
        }
    }
}
