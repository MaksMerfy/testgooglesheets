package ru.mb.analytics.maintest.controller;

import org.springframework.web.bind.annotation.GetMapping;

public interface MainTestController {
    @GetMapping("/")
    String getUrlSheets();
}
