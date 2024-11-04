package com.testWorkForAlfa.app;

import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    @RequestLog
    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return "Hello " + name;
    }

    @RequestLog
    @PostMapping("/bye")
    public String bye(@RequestParam String name) {return "Bye " + name;}
}


