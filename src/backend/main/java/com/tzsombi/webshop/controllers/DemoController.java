package com.tzsombi.webshop.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    public String get() {
        return """
                {
                    "name": "Wuhuu",
                    "email": "wuhuhu@gmail.com"
                }""";
    }
}
