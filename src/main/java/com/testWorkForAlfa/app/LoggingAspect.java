package com.testWorkForAlfa.app;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Component
public class LoggingAspect {

    @Before("@annotation(LogGet)")
    public void logGet(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        System.out.println("GET - запрос к методу: " + methodName);
        System.out.print("Аргументы : ");
        for (Object arg : args) {
            System.out.println(arg + ", ");
        }
        System.out.println();
    }
}
