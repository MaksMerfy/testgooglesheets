package ru.mb.analytics;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.*;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class AnalyticsApplication {

    private static Sheets sheetsService;
    private static Drive driveService;
    private static NetHttpTransport HTTP_TRANSPORT;
    private static String SPREADSHEET_ID = "1m8m7L9kJJD7byRblGddu14RygAliTfRBEMDy23y3J7M";
    private static final String APPLICATION_NAME = "Google Sheets Example";
    private static final LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

    public static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = AnalyticsApplication.class.getResourceAsStream("/credentials.json");
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);
        Credential credential = GoogleCredential.fromStream(in).createScoped(scopes);
        return credential;
    }

    public static Drive getDriveService() throws IOException, GeneralSecurityException {
        InputStream in = AnalyticsApplication.class.getResourceAsStream("/credentials.json");
        List<String> scopes = Arrays.asList(SheetsScopes.DRIVE);
        GoogleCredential credential = GoogleCredential.fromStream(in).createScoped(scopes);

        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Drive drive = new Drive.Builder(HTTP_TRANSPORT, JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();

        return drive;
    }

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        sheetsService = getSheetsService();
        driveService = getDriveService();

        com.google.api.services.drive.model.File  file = new com.google.api.services.drive.model.File();
        file.setName("test");
        file.setMimeType("application/vnd.google-apps.spreadsheet");

        Permission userPermission = new Permission()
                .setType("user")
                .setRole("writer")
                .setEmailAddress("maya.bioroboctis@gmail.com");

        Drive.Files.Create insert = driveService.files().create(file).setFields("id");
        file = insert.execute();

        JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
            @Override
            public void onFailure(GoogleJsonError e,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                // Handle error
                System.err.println(e.getMessage());
            }

            @Override
            public void onSuccess(Permission permission,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                System.out.println("Permission ID: " + permission.getId());
            }
        };
        BatchRequest batch = driveService.batch();
        driveService.permissions().create(file.getId(), userPermission)
                .setFields("id")
                .queue(batch, callback);
        batch.execute();

        Spreadsheet spreadSheet = new Spreadsheet().setProperties(
                new SpreadsheetProperties().setTitle("My Spreadsheet"));

        Spreadsheet result = sheetsService
                .spreadsheets()
                .get(file.getId()).execute();


        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList("Expenses January"),

                        Arrays.asList("shoes", "10")));
        UpdateValuesResponse result1 = sheetsService.spreadsheets().values()
                .update(result.getSpreadsheetId(), "A1", body)
                .setValueInputOption("RAW")
                .execute();

        System.out.println(result.getSpreadsheetUrl());
        receiver.stop();
    }

}
