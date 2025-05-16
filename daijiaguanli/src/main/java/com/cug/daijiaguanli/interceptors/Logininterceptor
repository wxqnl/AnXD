package com.cug.daijiaguanli.interceptors;


import com.cug.daijiaguanli.utils.JwtUtil;
import com.cug.daijiaguanli.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception{
        String token=request.getHeader("Authorization");
        try {
            //查看redis中的token
            ValueOperations<String,String>operations=stringRedisTemplate.opsForValue();
            String redisToken=operations.get(token);
            if(redisToken==null){
                throw new RuntimeException();
            }
            Map<String,Object> claims= JwtUtil.parseToken(token);
            //直接把jwt数据存到ThreadLocal中
            System.out.println("存储"+claims);
            ThreadLocalUtil.set(claims);
            return true;
        }catch (Exception e)
        {
            response.setStatus(401);
            return false;
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }

}
