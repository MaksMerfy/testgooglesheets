package ru.mb.analytics.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Service;
import ru.mb.analytics.AnalyticsApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Service
public class SheetService {
    private static final String APPLICATION_NAME = "Google Sheets Example";
    private static Sheets googleSheetService;

    public SheetService() throws GeneralSecurityException, IOException {
        googleSheetService = getSheetsService();
    }

    private static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static Credential authorize() throws IOException {
        InputStream in = AnalyticsApplication.class.getResourceAsStream("/credentials.json");
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
        return GoogleCredential.fromStream(in).createScoped(scopes);
    }

    public Spreadsheet createExampleSheet(com.google.api.services.drive.model.File file) throws IOException {
        Spreadsheet result = googleSheetService
                .spreadsheets()
                .get(file.getId()).execute();

        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("Expenses January"),

                        Arrays.asList("shoes", "10")));
        googleSheetService.spreadsheets().values()
                .update(result.getSpreadsheetId(), "A1", body)
                .setValueInputOption("RAW")
                .execute();

        return result;
    }
}
