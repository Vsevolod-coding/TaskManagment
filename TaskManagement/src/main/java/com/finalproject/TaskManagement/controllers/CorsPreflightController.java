package com.finalproject.TaskManagement.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CorsPreflightController {
    @RequestMapping(method = RequestMethod.OPTIONS)
    public void handleOptions() {
        // Ничего не делаем, просто позволяем браузеру завершить preflight
    }
}
