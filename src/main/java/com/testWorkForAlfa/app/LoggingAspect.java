package com.testWorkForAlfa.app;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(RequestLog)")
    public void RequestLog(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        GetMapping getMapping = method.getAnnotation(GetMapping.class);

        String[] values = getMapping.value();
        System.out.println("GET-запрос к методу " + Arrays.toString(values));

        Object[] args = joinPoint.getArgs();
        System.out.println("Аргументы : ");
        for (Object arg : args) {
            System.out.println(arg + " ");
        }
        System.out.println();
    }
}
