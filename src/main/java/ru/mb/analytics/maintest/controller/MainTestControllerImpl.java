package ru.mb.analytics.maintest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.mb.analytics.account.service.AccountService;


@RestController
public class MainTestControllerImpl implements MainTestController {
   @Autowired
   private AccountService accountService;

    @Override
    public String getUrlSheets() {
        return accountService.getFileOfAllAccount();
    }
}
