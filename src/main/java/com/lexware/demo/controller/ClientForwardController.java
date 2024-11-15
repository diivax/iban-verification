package com.lexware.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ClientForwardController {
    @GetMapping(value = "/**/{path:[^\\.]*}")
    public String forward(@PathVariable("path") String path) {
        return "forward:/";
    }
}
