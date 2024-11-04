package com.testWorkForAlfa.app;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final CacheService cacheService;

    public LoggingAspect(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Around("@annotation(getMapping) && @annotation(requestLog)")
    public Object cacheAndLogRequest(ProceedingJoinPoint joinPoint, GetMapping getMapping, RequestLog requestLog) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        System.out.println("метод который используется " + methodName);
        System.out.println("с аргументами " + Arrays.toString(args));

        // Создаем уникальный ключ для кэша
        String cacheKey = methodName + Arrays.hashCode(args);

        // Проверяем кэш Redis
        Object cachedResult = cacheService.getFromCache(cacheKey);
        if (cachedResult != null) {
            System.out.println("Ответ из кэша Redis для метода: " + methodName);
            return cachedResult;
        }

        // Выполняем метод и сохраняем результат
        Object result = joinPoint.proceed();

        // Кэшируем результат и сохраняем в MongoDB
        cacheService.saveToCache(cacheKey, result);
        System.out.println("данные добавленные в кэш: " + result);
        cacheService.saveToMongo(methodName, result, args);
        System.out.println("данные добавленные в базу данных: метод " + methodName + ", с аргументами " + Arrays.toString(args));

        return result;
    }
}