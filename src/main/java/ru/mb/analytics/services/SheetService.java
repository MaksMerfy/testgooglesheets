package ru.mb.analytics.services;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SheetService {

    private static Sheets googleSheetService;

    static {
        try {
            googleSheetService = new Sheets.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), new GoogleService(SheetsScopes.SPREADSHEETS).authorize())
                    .setApplicationName(GoogleService.APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @SneakyThrows
    public Spreadsheet createExampleSheet(com.google.api.services.drive.model.File file) throws IOException {
        Spreadsheet result = googleSheetService
                .spreadsheets()
                .get(file.getId()).execute();

        Sheets.Spreadsheets.Values.Update resultUpdate = googleSheetService.spreadsheets().values()
                .update(result.getSpreadsheetId(), "A1", null)
                .setValueInputOption("RAW");

        int stepOfTry = 0;
        Spreadsheet resultForReturn = null;
        while (stepOfTry < 5) {
            try {
                resultUpdate.execute();
                resultForReturn = result;
                break;
            } catch (IOException e) {
                logger.error(e.getMessage());
                TimeUnit.SECONDS.sleep(5);
                stepOfTry++;
            }
        }
        return resultForReturn;
    }

    public boolean writeToASingleRange(com.google.api.services.drive.model.File file, List arrayOfValue) throws IOException, InterruptedException {
        return writeToASingleRangeGoogle(file, arrayOfValue, "A1");
    }

    public boolean writeToASingleRange(com.google.api.services.drive.model.File file, List arrayOfValue, String range) throws IOException, InterruptedException {
        return writeToASingleRangeGoogle(file, arrayOfValue, range);
    }

    private boolean writeToASingleRangeGoogle(com.google.api.services.drive.model.File file, List arrayOfValue, String range) throws IOException, InterruptedException {
        ValueRange body = new ValueRange()
                .setValues(arrayOfValue);
        Sheets.Spreadsheets.Values.Update resultUpdate = googleSheetService.spreadsheets().values()
                .update(file.getId(), range, body)
                .setValueInputOption("RAW");

        boolean succes = false;

        int stepOfTry = 0;
        while (stepOfTry < 5) {
            try {
                resultUpdate.execute();
                succes = true;
                break;
            } catch (IOException e) {
                logger.error(e.getMessage());
                TimeUnit.SECONDS.sleep(5);
                stepOfTry++;
            }
        }

        return succes;
    }

    public boolean writeToAMultipleRanges(com.google.api.services.drive.model.File file, ValueRange data) throws IOException, InterruptedException {

        Sheets.Spreadsheets.Values.Append resultUpdate =
                googleSheetService.spreadsheets().values()
                        .append(file.getId(), "Sheet1", data)
                        .setValueInputOption("RAW");
        boolean succes = false;

        int stepOfTry = 0;
        while (stepOfTry < 5) {
            try {
                resultUpdate.execute();
                succes = true;
                break;
            } catch (IOException e) {
                logger.error(e.getMessage());
                TimeUnit.SECONDS.sleep(5);
                stepOfTry++;
            }
        }

        return succes;
    }
}
