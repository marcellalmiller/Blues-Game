import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

public class GoogleSheetsLiveTest {
  private static Sheets sheetsService;
  private static String SPREADSHEET_ID = "1LGbmhZo9iNPJNaxdtkv2BLvaFhsBUJhf2Q_rC3ms6GE";

  @BeforeClass
  public static void setup() throws GeneralSecurityException, IOException {
    sheetsService = SheetsServiceUtil.getSheetsService();
  }

  @Test
  public void whenWriteSheet_thenReadSheetOk() throws IOException {
    ValueRange body = new ValueRange().setValues(Arrays.asList(
            Arrays.asList("Expenses January"),
            Arrays.asList("books", "30"),
            Arrays.asList("pens", "10"),
            Arrays.asList("Expenses February"),
            Arrays.asList("clothes", "20"),
            Arrays.asList("shoes", "5")));

    UpdateValuesResponse result = sheetsService.spreadsheets().values()
            .update(SPREADSHEET_ID, "A1", body)
            .setValueInputOption("RAW")
            .execute();
  }
}
