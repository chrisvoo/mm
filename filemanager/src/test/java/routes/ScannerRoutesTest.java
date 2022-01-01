package routes;

import models.Model;
import models.files.MusicFile;
import models.files.MusicFileSchema;
import models.scanner.ScanRequest;
import org.junit.jupiter.api.*;
import routes.utils.PaginatedResponse;
import src.DbHelper;
import src.FileManagerClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScannerRoutesTest {
    private static FileManagerClient client;

    @BeforeAll
    static void initAll() {
        client = new FileManagerClient();
        DbHelper.emptyTable(new MusicFileSchema().tableName());
    }

    @AfterAll
    static void tearDown() {
        DbHelper.emptyTable(new MusicFileSchema().tableName());
    }

    @Test @Order(1)
    public void scanRequestTest(TestReporter rep) throws URISyntaxException, IOException, InterruptedException {
        String json = """
              {
                "directory": "/home/christian/GitHub/mm/filemanager/src/test/resources/tree"
              }
          """;
        HttpResponse<String> response = client.sendPost("/scanner/scan", json);
        assertEquals("true", response.body());

        HttpResponse<String> getAllResponse = client.sendGet(
          "/files/list",
          new HashMap<>(
            Map.of(
              "sort_dir", "asc",
              "count", "15"
            )
          )
        );

        // The backend doesn't block the client, just answers true and starts to scan
        PaginatedResponse<MusicFile> pRes = PaginatedResponse.fromJson(getAllResponse.body(), MusicFile.class);
        List<MusicFile> items = pRes.getItems();
        assertTrue(items.size() < 5);

        Thread.sleep(1000);

        getAllResponse = client.sendGet(
          "/files/list",
          new HashMap<>(
            Map.of(
              "sort_dir", "asc",
              "count", "15"
            )
          )
        );
        pRes = PaginatedResponse.fromJson(getAllResponse.body(), MusicFile.class);
        items = pRes.getItems();
        assertEquals(14, items.size());
    }

    @Test
    public void testDefaultDir() {
        ScanRequest scan = Model.fromJson("{}", ScanRequest.class);
        assertNotNull(scan);

        scan = Model.fromJson("", ScanRequest.class);
        assertNull(scan);
    }
}