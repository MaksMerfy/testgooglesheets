package ru.mb.analytics.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import ru.mb.analytics.AnalyticsApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class GoogleService {
    static final String APPLICATION_NAME = "Google Sheets Example";
    private static String sheetsScopes;

    public GoogleService(String sheetsScopes) {
        this.sheetsScopes = sheetsScopes;
    }

    public static Credential authorize() throws IOException {
        InputStream in = AnalyticsApplication.class.getResourceAsStream("/credentials.json");
        List<String> scopes = Arrays.asList(sheetsScopes);
        return GoogleCredential.fromStream(in).createScoped(scopes);
    }
}
