package routes;

import models.files.MusicFileSchema;
import org.junit.jupiter.api.*;
import src.DbHelper;
import src.FileManagerClient;

import java.io.IOException;
import java.net.URISyntaxException;

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
    public void scanRequestTest() throws URISyntaxException, IOException, InterruptedException {

    }

}