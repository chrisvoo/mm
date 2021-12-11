package routes;

import exceptions.DbException;
import models.band.Band;
import models.band.BandSchema;
import models.utils.ErrorResponse;
import org.junit.jupiter.api.*;
import src.DbHelper;
import src.FileManagerClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BandRoutesTest {
    private static FileManagerClient client;
    private Long bandId;

    @BeforeAll
    static void initAll() {
        client = new FileManagerClient();
        DbHelper.emptyTable(new BandSchema().tableName());
    }

    @AfterAll
    static void tearDown() {
        DbHelper.emptyTable(new BandSchema().tableName());
    }

    @Test @Order(1)
    public void getBandByIdTest() throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = client.sendGet("/bands/band/5");
        assertEquals(200, response.statusCode());
        ErrorResponse err = ErrorResponse.fromJson(response.body());

        assertEquals("No band with id 5 was found", err.getMessage());
        assertEquals(DbException.RESOURCE_NOT_FOUND, err.getCode());
    }

    @Test @Order(2)
    public void createBandTest(TestReporter rep) throws URISyntaxException, IOException, InterruptedException {
        String json = """
            {
                "name": "AC/DC",
                "country": "AU",
                "countryName": "Australia",
                "activeFrom": 1973,
                "totalAlbumsReleased": 18,
                "website": "",
                "twitter": ""
            }       
        """;
        HttpResponse<String> response = client.sendPost("/bands/band", json);
        String originalBody = response.body();
        rep.publishEntry(originalBody);

        Band band = Band.fromJson(originalBody);
        assertInstanceOf(Long.class, band.getId());
        assertEquals("AC/DC", band.getName());
        assertEquals("AU", band.getCountry());
        assertEquals("Australia", band.getCountryName());
        assertEquals(1973, band.getActiveFrom().shortValue());
        assertNull(band.getActiveTo());
        assertEquals(18, band.getTotalAlbumsReleased().shortValue());
        assertTrue(band.getTwitter().isEmpty());
        assertTrue(band.getWebsite().isEmpty());

        bandId = band.getId();
    }

    @Test @Order(3)
    public void updateBandTest(TestReporter rep) throws URISyntaxException, IOException, InterruptedException {
        String json = """
            {
                "name": "AC/DC",
                "country": "US",
                "countryName": "United States",
                "activeFrom": 1973,
                "totalAlbumsReleased": 18,
                "website": "http://website.com"
            }
        """;
        assertNotNull(this.bandId);
        HttpResponse<String> response = client.sendPut("/bands/band/" + this.bandId, json);
        String originalBody = response.body();
        rep.publishEntry(originalBody);

        Band band = Band.fromJson(response.body());
        assertInstanceOf(Long.class, band.getId());
        assertEquals("AC/DC", band.getName());
        assertEquals("US", band.getCountry());
        assertEquals("United States", band.getCountryName());
        assertEquals(1973, band.getActiveFrom().shortValue());
        assertNull(band.getActiveTo());
        assertEquals(18, band.getTotalAlbumsReleased().shortValue());
        assertNull(band.getTwitter());
        assertEquals("http://website.com", band.getWebsite());
    }
}