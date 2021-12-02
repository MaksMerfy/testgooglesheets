package ru.mb.analytics.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public interface MainTestRestController {
    @GetMapping("/")
    String getUrlSheets();
}
