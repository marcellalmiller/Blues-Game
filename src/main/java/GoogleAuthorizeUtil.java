import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class GoogleAuthorizeUtil {
  public static Credential authorizeMyAcc() throws IOException, GeneralSecurityException {
    // build GoogleClientSecrets from JSON file
    InputStream in =
            GoogleAuthorizeUtil.class.getResourceAsStream("/google-sheets-client-secret.json");
    GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));

    List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

    // build Credential object
    GoogleAuthorizationCodeFlow flow =
            new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
                    .setDataStoreFactory(new MemoryDataStoreFactory())
                    .setAccessType("offline").build();
    Credential credential =
            new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

    return credential;
  }

  public static ServiceAccountCredentials authorizeServiceAcc() throws IOException, GeneralSecurityException {
    FileInputStream credentialsStream = new FileInputStream("src/main/resources/google-sheets-service-account.json");
    return ServiceAccountCredentials.fromStream(credentialsStream);
  }
}
