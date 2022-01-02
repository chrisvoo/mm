package routes;

import models.Model;
import models.stats.Stats;
import models.stats.StatsSchema;
import org.junit.jupiter.api.*;
import src.DbHelper;
import src.FileManagerClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatsRoutesTest {
    private static FileManagerClient client;

    @BeforeAll
    static void initAll() {
        client = new FileManagerClient();
        DbHelper.emptyTable(new StatsSchema().tableName());
    }

    @AfterAll
    static void tearDown() {
        DbHelper.emptyTable(new StatsSchema().tableName());
    }

    @Test @Order(1)
    public void statsTest(TestReporter rep) throws URISyntaxException, IOException, InterruptedException {
        String json = """
              {
                "totalFiles": 14,
                "totalBytes": 1254785
              }
          """;
        HttpResponse<String> response = client.sendPost("/stats", json);
        Stats stats = Model.fromJson(response.body(), Stats.class);
        rep.publishEntry(response.body());

        assertEquals(14, stats.getTotalFiles());
        assertEquals(1254785, stats.getTotalBytes());
        assertNotNull(stats.getId());
        assertNotNull(stats.getLastUpdate());

        HttpResponse<String> getStatsResponse = client.sendGet("/stats");
        stats = Model.fromJson(getStatsResponse.body(), Stats.class);
        assertEquals(14, stats.getTotalFiles());
        assertEquals(1254785, stats.getTotalBytes());
        assertNotNull(stats.getId());
        assertNotNull(stats.getLastUpdate());
    }
}