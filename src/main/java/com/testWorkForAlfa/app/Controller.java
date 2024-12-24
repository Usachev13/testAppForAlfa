package com.testWorkForAlfa.app;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    private final HelloService helloService;

    public Controller(HelloService helloService) {
        this.helloService = helloService;
    }

    @RequestLog
    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return helloService.hello(name);
    }

    @RequestLog
    @PostMapping("/bye")
    public String bye(@RequestParam String name) {return "Bye " + name;}
}


