package ru.mb.analytics.services;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.sheets.v4.SheetsScopes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DriveService {
    private static Drive googleDriveService;

    static {
        try {
            googleDriveService = new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), new GoogleService(SheetsScopes.DRIVE).authorize())
                    .setApplicationName(GoogleService.APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public com.google.api.services.drive.model.File createFileOnDrive(String nameFile) throws IOException, InterruptedException {
        com.google.api.services.drive.model.File file = new com.google.api.services.drive.model.File();
        file.setName(nameFile);
        file.setMimeType("application/vnd.google-apps.spreadsheet");

        Drive.Files.Create insert = googleDriveService.files().create(file).setFields("id");

        int stepOfTry = 0;
        while (stepOfTry < 5) {
            try {
                file = insert.execute();
                break;
            } catch (IOException e) {
                logger.error(e.getMessage());
                TimeUnit.SECONDS.sleep(5);
                stepOfTry++;
                file = null;
            }
        }
        return file;
    }

    public Boolean addPermisionOnFile(com.google.api.services.drive.model.File file, String mail) throws IOException, InterruptedException {
        Permission userPermission = new Permission()
                .setType("user")
                .setRole("writer")
                .setEmailAddress(mail);

        JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
            @Override
            public void onFailure(GoogleJsonError e,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                logger.error("Failed add permission");
            }

            @Override
            public void onSuccess(Permission permission,
                                  HttpHeaders responseHeaders)
                    throws IOException {
                logger.info("Success add permission");
            }
        };
        BatchRequest batch = googleDriveService.batch();
        googleDriveService.permissions().create(file.getId(), userPermission)
                .setFields("id")
                .queue(batch, callback);

        Boolean succes = false;

        int stepOfTry = 0;
        while (stepOfTry < 5) {
            try {
                batch.execute();
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
