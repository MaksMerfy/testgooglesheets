package ru.mb.analytics;

import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.mb.analytics.services.DriveService;
import ru.mb.analytics.services.SheetService;

import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
public class AnalyticsApplication {
    private static final LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        DriveService driveService = new DriveService();
        SheetService sheetsService = new SheetService();

        com.google.api.services.drive.model.File file = driveService.createFileOnDrive("File for test");
        driveService.addPermisionOnFile(file, "maya.bioroboctis@gmail.com");
        Spreadsheet result = sheetsService.createExampleSheet(file);
        System.out.println(result.getSpreadsheetUrl());
        receiver.stop();
    }

}
