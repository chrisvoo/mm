package routes;

import exceptions.DbException;
import models.band.BandSchema;
import models.utils.ErrorResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import src.DbHelper;
import src.FileManagerClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class BandRoutesTest {
    private static FileManagerClient client;
    private static final Logger logger = Logger.getLogger(BandRoutesTest.class.getName());


    @BeforeAll
    static void initAll() {
        client = new FileManagerClient();
        DbHelper.emptyTable(new BandSchema().tableName());
    }

    @AfterAll
    static void tearDown() {
        DbHelper.emptyTable(new BandSchema().tableName());
    }

    @Test
    public void getBandByIdTest() throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = client.sendGet("/bands/band/5");
        Assertions.assertEquals(200, response.statusCode());
        ErrorResponse err = ErrorResponse.fromJson(response.body());

        Assertions.assertEquals("No band with id 5 was found", err.getMessage());
        Assertions.assertEquals(DbException.RESOURCE_NOT_FOUND, err.getCode());
    }
}