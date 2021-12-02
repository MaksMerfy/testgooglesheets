package ru.mb.analytics.controllers.impl;

import com.google.api.services.drive.model.File;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import ru.mb.analytics.controllers.MainTestRestController;
import ru.mb.analytics.services.DriveService;
import ru.mb.analytics.services.SheetService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
public class MainRestController implements MainTestRestController {
    @Autowired
    private SheetService sheetService;

    @Autowired
    private DriveService driveService;

    @Override
    public String getUrlSheets() {
        String returnedId = "Some going not good";
        try {
            File file = driveService.createFileOnDrive("Test spring boot");
            if (file == null) return returnedId;

            Boolean succes = driveService.addPermisionOnFile(file, "maya.bioroboctis@gmail.com");
            if (succes == false) return returnedId;

            Spreadsheet result = sheetService.createExampleSheet(file);
            if (result == null) return returnedId;

            List listOfValue = Arrays.asList(
                    Arrays.asList("", "Всего", "01.12-04.12"),
                    Arrays.asList("Отклики", "1548", "360"));
            succes = sheetService.writeToASingleRange(file, listOfValue);
            if (succes == false) return returnedId;

            returnedId = "https://docs.google.com/spreadsheets/d/" + file.getId() + "/edit";
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return returnedId;
    }
}
