package com.testWorkForAlfa.app;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
    public class HelloService {

        @Cacheable(value = "requests", key = "#root.methodName + T(java.util.Arrays).hashCode(#root.args)")
        public String hello(String name) {
            return "Hello " + name;
        }
    }
