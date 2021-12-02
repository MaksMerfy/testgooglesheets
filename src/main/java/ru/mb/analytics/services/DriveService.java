package ru.mb.analytics.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.stereotype.Service;
import ru.mb.analytics.AnalyticsApplication;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Service
public class DriveService {
    private Drive googleDriveService;
    private static final String APPLICATION_NAME = "Google Sheets Example";

    public DriveService() throws GeneralSecurityException, IOException {
        googleDriveService = getDriveService();
    }

    private static Drive getDriveService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Drive.Builder(HTTP_TRANSPORT, JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static Credential authorize() throws IOException {
        InputStream in = AnalyticsApplication.class.getResourceAsStream("/credentials.json");
        List<String> scopes = Arrays.asList(SheetsScopes.DRIVE);
        return GoogleCredential.fromStream(in).createScoped(scopes);
    }

    public com.google.api.services.drive.model.File createFileOnDrive(String nameFile) throws IOException {
        com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
        file.setName(nameFile);
        file.setMimeType("application/vnd.google-apps.spreadsheet");

        Drive.Files.Create insert = googleDriveService.files().create(file).setFields("id");

        return insert.execute();
    }

    public void addPermisionOnFile(com.google.api.services.drive.model.File file, String mail) throws IOException {
        Permission userPermission = new Permission()
                .setType("user")
                .setRole("writer")
                .setEmailAddress(mail);

        JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
            @Override
            public void onFailure(GoogleJsonError e,
                                  HttpHeaders responseHeaders)
                    throws IOException {
            }

            @Override
            public void onSuccess(Permission permission,
                                  HttpHeaders responseHeaders)
                    throws IOException {
            }
        };
        BatchRequest batch = googleDriveService.batch();
        googleDriveService.permissions().create(file.getId(), userPermission)
                .setFields("id")
                .queue(batch, callback);
        batch.execute();
    }
}
