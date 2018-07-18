package com.spring.boot.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * 统一异常处理类；所有Controller中的异常都会到这里
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object globalErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("code", 100);
        hashMap.put("message", e.getMessage());
        hashMap.put("url", request.getRequestURL().toString());
        hashMap.put("data", "请求失败！");

        LOGGER.error("请求异常：" + objectMapper.writeValueAsString(hashMap));

        return hashMap;
    }
}
