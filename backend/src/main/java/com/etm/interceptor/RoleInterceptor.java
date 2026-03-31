package com.etm.interceptor;

import com.etm.annotation.RequireRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;

        // 检查方法级注解
        RequireRole methodAnnotation = method.getMethodAnnotation(RequireRole.class);
        // 检查类级注解
        RequireRole classAnnotation = method.getBeanType().getAnnotation(RequireRole.class);

        RequireRole requireRole = methodAnnotation != null ? methodAnnotation : classAnnotation;

        if (requireRole == null) {
            return true; // 无角色限制
        }

        String userRole = (String) request.getAttribute("role");
        if (userRole == null) {
            sendError(response, 403, "无权访问");
            return false;
        }

        boolean hasRole = Arrays.asList(requireRole.value()).contains(userRole);
        if (!hasRole) {
            sendError(response, 403, "无权访问该资源");
            return false;
        }

        return true;
    }

    private void sendError(HttpServletResponse response, int code, String message) throws Exception {
        response.setStatus(code);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> result = new HashMap<>();
        result.put("code", code);
        result.put("message", message);
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
