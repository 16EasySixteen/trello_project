package com.example.trelloproject.user.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

public class PathVariableExtractor {

    public static Long extractPathVariable(HttpServletRequest request, String variableName) {
        // HandlerMapping에서 경로 변수 가져오기
        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (pathVariables != null && pathVariables.containsKey(variableName)) {
            try {
                return Long.valueOf(pathVariables.get(variableName)); // Long 타입으로 변환
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid path variable format for: " + variableName, e);
            }
        }
        return 0L;
    }
}
