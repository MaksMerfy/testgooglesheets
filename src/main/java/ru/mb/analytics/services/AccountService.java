package ru.mb.analytics.services;

import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mb.analytics.entity.Account;
import ru.mb.analytics.repository.AccountRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AccountService {
    @Autowired
    private SheetService sheetService;
    @Autowired
    private DriveService driveService;
    @Autowired
    private AccountRepository accountRepository;

    public String getFileOfAllAccount() {
        String returnedId = "Some going not good";
        try {
            File file = driveService.createFileOnDrive("Test spring boot");
            if (file == null) return returnedId;

            boolean succes = driveService.addPermisionOnFile(file, "maya.bioroboctis@gmail.com");
            if (succes == false) return returnedId;

            Spreadsheet result = sheetService.createExampleSheet(file);
            if (result == null) return returnedId;

            List<Account> listAccount = accountRepository.getResponseJobListCandidateWithoutMailing();

            //Сделаем первую колонку
            List<List<Object>> values = new ArrayList<>();
            for (Account account : listAccount) {
                List<Object> newLine = new ArrayList<>();
                newLine.add(account.getLogin());
                values.add(newLine);
            }

            //Допихнем все в другие колонки
            int range = 0;
            while (range < values.size()){
                List<Object> currentLine = values.get(range);
                Account currentAccount = listAccount.stream().filter(account -> account.getLogin().equals(currentLine.get(0))).findAny().orElse(null);
                if (currentAccount == null) continue;
                currentLine.add((currentAccount.getMail()==null) ? "": currentAccount.getMail());
                currentLine.add((currentAccount.getSite()==null) ? "": currentAccount.getSite());
                range++;
            }

            ValueRange body = new ValueRange()
                    .setValues(values);

            succes = sheetService.writeToAMultipleRanges(file, body);
            if (succes == false) return returnedId;

            succes = sheetService.tryFormatRanges(file,
                    0,
                    2,
                    6,
                    1,
                    2,
                    new Color().setRed(0.8f));
            if (succes == false) return returnedId;

            returnedId = "https://docs.google.com/spreadsheets/d/" + file.getId() + "/edit";
        } catch (IOException e) {
            logger.error(e.toString());
        } catch (InterruptedException e) {
            logger.error(e.toString());
        }
        return returnedId;
    }
}
