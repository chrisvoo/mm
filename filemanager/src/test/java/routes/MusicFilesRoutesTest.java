package routes;

import exceptions.DbException;
import models.files.BitRateType;
import models.files.MusicFile;
import models.files.MusicFileSchema;
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
public class MusicFilesRoutesTest {
    private static FileManagerClient client;
    private Long fileId;

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
    public void getFileByIdTest() throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = client.sendGet("/files/file/5");
        assertEquals(200, response.statusCode());
        ErrorResponse err = ErrorResponse.fromJson(response.body());

        assertEquals("No file with id 5 was found", err.getMessage());
        assertEquals(DbException.RESOURCE_NOT_FOUND, err.getCode());
    }

    @Test @Order(2)
    public void createFileTest(TestReporter rep) throws URISyntaxException, IOException, InterruptedException {
        String json = """
              {
                "absolutePath": "/data/file.mp3",
                "size": 12457856,
                "bitrate": 320,
                "bitRateType": "CBR",
                "duration": 258,
                "artist": "Nirvana",
                "album": "In Utero",
                "year": 1993,
                "genre": "grunge",
                "title": "Serve the servant"
              }
          """;
        HttpResponse<String> response = client.sendPost("/files/file", json);
        String originalBody = response.body();
        rep.publishEntry(originalBody);

        MusicFile file = MusicFile.fromJson(originalBody);
        assertInstanceOf(Long.class, file.getId());
        assertEquals("/data/file.mp3", file.getAbsolutePath());
        assertEquals(12457856, file.getSize());
        assertEquals("Nirvana", file.getArtist());
        assertEquals("In Utero", file.getAlbum());
        assertEquals(258, file.getDuration());
        assertEquals(BitRateType.CBR, file.getBitRateType());
        assertEquals(320, file.getBitrate());
        assertNull(file.getAlbumImage());
        assertEquals(1993, file.getYear().shortValue());
        assertEquals("grunge", file.getGenre());
        assertEquals("Serve the servant", file.getTitle());

        fileId = file.getId();
    }

    @Test @Order(3)
    public void updateFileTest(TestReporter rep) throws URISyntaxException, IOException, InterruptedException {
        String json = """
              {
                "absolutePath": "/data/file.mp3",
                "size": 12457857,
                "bitrate": 256,
                "bitRateType": "VBR",
                "duration": 281,
                "artist": "Nirvana",
                "album": "In Utero",
                "year": 1993,
                "genre": "grunge",
                "title": "Heart-Shaped Box"
              }
          """;
        assertNotNull(this.fileId);
        HttpResponse<String> response = client.sendPut("/files/file/" + this.fileId, json);
        String originalBody = response.body();
        rep.publishEntry(originalBody);

        MusicFile file = MusicFile.fromJson(originalBody);
        assertInstanceOf(Long.class, file.getId());
        assertEquals("/data/file.mp3", file.getAbsolutePath());
        assertEquals(12457857, file.getSize());
        assertEquals("Nirvana", file.getArtist());
        assertEquals("In Utero", file.getAlbum());
        assertEquals(281, file.getDuration());
        assertEquals(BitRateType.VBR, file.getBitRateType());
        assertEquals(256, file.getBitrate());
        assertNull(file.getAlbumImage());
        assertEquals(1993, file.getYear().shortValue());
        assertEquals("grunge", file.getGenre());
        assertEquals("Heart-Shaped Box", file.getTitle());
    }
}