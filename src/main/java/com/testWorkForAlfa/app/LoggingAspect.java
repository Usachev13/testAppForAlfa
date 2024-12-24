package com.testWorkForAlfa.app;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final RequestLogRepository requestLogRepository;

    public LoggingAspect(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }


    @Around(value = "@annotation(getMapping) && @annotation(requestLog)", argNames = "joinPoint,getMapping,requestLog")
    public Object cacheAndLogRequest(ProceedingJoinPoint joinPoint, GetMapping getMapping, RequestLog requestLog) throws Throwable {
        Object result = joinPoint.proceed();

        // Сохраняем в MongoDB через репозиторий
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        RequestLogEntry entry = new RequestLogEntry();
        entry.setMethodName(methodName);
        entry.setArgs(Arrays.toString(args));
        entry.setResult(result);
        requestLogRepository.save(entry);

        System.out.println("Сохранено в MongoDB: " + entry);
        return result;
    }
}