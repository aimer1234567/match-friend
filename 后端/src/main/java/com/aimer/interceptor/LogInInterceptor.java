package com.aimer.interceptor;

import com.aimer.commen.BaseContext;
import com.aimer.constant.JWTConstant;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;

@Slf4j
public class LogInInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("Method:OPTIONS");
            return true;
        }
        String token = request.getHeader("Token");
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT verify = verifier.verify(token);
            Long user=verify.getClaim(JWTConstant.USERID).asLong();
            Date expiresAt = verify.getExpiresAt();
            if(expiresAt.before(new Date())){
                response.setStatus(401);
                return false;
            }
            log.info(String.valueOf("id："+user));
            BaseContext.setId(user);
            return true;
        }catch (JWTVerificationException | NullPointerException e){
            response.setStatus(401);
            return false;
        }
    }
}
