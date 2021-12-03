package ru.mb.analytics.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.mb.analytics.controllers.MainTestRestController;
import ru.mb.analytics.services.AccountService;


@RestController
public class MainRestController implements MainTestRestController {
   @Autowired
   private AccountService accountService;

    @Override
    public String getUrlSheets() {
        return accountService.getFileOfAllAccount();
    }
}
