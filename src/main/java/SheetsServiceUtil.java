import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SheetsServiceUtil {
  private static final String APPLICATION_NAME = "Blues Game";

  public static Sheets getSheetsService() throws GeneralSecurityException, IOException {
    ServiceAccountCredentials credentials = GoogleAuthorizeUtil.authorizeServiceAcc();
    return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
            JacksonFactory.getDefaultInstance(), new HttpCredentialsAdapter(credentials))
            .setApplicationName(APPLICATION_NAME)
            .build();
  }
}
