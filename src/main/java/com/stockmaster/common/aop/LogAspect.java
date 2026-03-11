package com.stockmaster.common.aop;

import com.stockmaster.common.enums.OperationType;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(com.stockmaster.common.aop.LogOperation)")
    public void logPointCut() {}

    @Before("logPointCut()")
    public void before(JoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String operationType = getLogOperation(point).value().getDesc();
        String detail = getLogOperation(point).detail();

        logger.info("================================= OPERATION START =================================");
        logger.info("Request URL: {}", request.getRequestURL().toString());
        logger.info("HTTP Method: {}", request.getMethod());
        logger.info("IP: {}", request.getRemoteAddr());
        logger.info("Class Method: {}.{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
        logger.info("Operation Type: {}", operationType);
        if (!detail.isEmpty()) {
            logger.info("Detail: {}", detail);
        }
        logger.info("Request Params: {}", Arrays.toString(point.getArgs()));
    }

    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void afterReturning(JoinPoint point, Object result) {
        logger.info("Response Result: {}", result);
        logger.info("================================= OPERATION END =================================");
    }

    private LogOperation getLogOperation(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(LogOperation.class);
    }
}