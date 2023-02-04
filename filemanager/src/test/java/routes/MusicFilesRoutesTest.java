package routes;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import exceptions.ModelException;
import models.files.MusicFile;
import models.files.MusicFileSchema;
import models.utils.ErrorResponse;
import org.junit.jupiter.api.*;
import routes.utils.PaginatedResponse;
import routes.utils.PaginationMetadata;
import services.MusicFileService;
import src.DbHelper;
import src.FileManagerClient;
import src.TestUtils;
import utils.di.FileManagerModule;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MusicFilesRoutesTest {
    private static FileManagerClient client;
    private Long fileId;

    @Inject private static MusicFileSchema schema;

    @BeforeAll
    static void initAll() {
        client = new FileManagerClient();
        DbHelper.emptyTable(schema.tableName());
    }

    @AfterAll
    static void tearDown() {

    }

    @Test @Order(1)
    public void getFileByIdTest() throws URISyntaxException, IOException, InterruptedException {
        HttpResponse<String> response = client.sendGet("/files/file/5");
        assertEquals(200, response.statusCode());
        ErrorResponse err = ErrorResponse.fromJson(response.body());

        assertEquals("No file with id 5 was found", err.getMessage());
        assertEquals(ModelException.INVALID_FIELDS, err.getCode());
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
        assertEquals(1993, file.getYear().shortValue());
        assertEquals("grunge", file.getGenre());
        assertEquals("Heart-Shaped Box", file.getTitle());
    }

    @Test @Order(4)
    public void testGetAll(TestReporter rep) throws URISyntaxException, IOException, InterruptedException {
        // clean and pre-fill the database with some data...
        DbHelper.emptyTable(MusicFileSchema.TABLE_NAME);
        Injector injector = Guice.createInjector(new FileManagerModule());

        List<MusicFile> files = new ArrayList<>();

        for (int i = 1; i < 32; i++) {
            files.add(
              new MusicFile()
                .setTitle("Title " + i)
                .setAbsolutePath(TestUtils.randomString())
            );
        }

        MusicFileService service = injector.getInstance(MusicFileService.class);
        assertEquals(31, service.bulkSave(files));

        // page 1
        HttpResponse<String> response = client.sendGet("/files/list", new HashMap<>(Map.of("sort_dir", "asc")));

        PaginatedResponse<MusicFile> pRes = PaginatedResponse.fromJson(response.body(), MusicFile.class);
        List<MusicFile> items = pRes.getItems();
        assertNotNull(items);
        assertEquals(10, items.size());
        assertEquals("Title 1", items.get(0).getTitle());
        assertEquals("Title 10", items.get(9).getTitle());

        PaginationMetadata meta = pRes.getMetadata();
        assertTrue(meta.getNextCursor() != null && !meta.getNextCursor().isBlank());
        assertTrue(meta.hasMoreData());
        assertEquals(31, meta.getTotalCount());
        assertNotNull(meta.getNextCursor());

        // page 2
        response = client.sendGet("/files/list", new HashMap<>(
          Map.of(
            "sort_dir", "asc",
            "cursor",
            meta.getNextCursor()
          ))
        );

        pRes = PaginatedResponse.fromJson(response.body(), MusicFile.class);
        items = pRes.getItems();
        assertNotNull(items);
        assertEquals(10, items.size());
        assertEquals("Title 11", items.get(0).getTitle());
        assertEquals("Title 20", items.get(9).getTitle());

        meta = pRes.getMetadata();
        assertTrue(meta.getNextCursor() != null && !meta.getNextCursor().isBlank());
        assertTrue(meta.hasMoreData());
        assertNull(meta.getTotalCount());

        // page 3
        response = client.sendGet("/files/list", new HashMap<>(
            Map.of(
              "sort_dir", "asc",
              "cursor",
              meta.getNextCursor())
          )
        );

        pRes = PaginatedResponse.fromJson(response.body(), MusicFile.class);
        items = pRes.getItems();
        assertNotNull(items);
        assertEquals(10, items.size());
        assertEquals("Title 21", items.get(0).getTitle());
        assertEquals("Title 30", items.get(9).getTitle());

        meta = pRes.getMetadata();
        assertTrue(meta.getNextCursor() != null && !meta.getNextCursor().isBlank());
        assertTrue(meta.hasMoreData());
        assertNull(meta.getTotalCount());

        // page 4
        response = client.sendGet("/files/list", new HashMap<>(
            Map.of(
              "sort_dir", "asc",
              "cursor",
              meta.getNextCursor())
          )
        );

        pRes = PaginatedResponse.fromJson(response.body(), MusicFile.class);
        items = pRes.getItems();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals("Title 31", items.get(0).getTitle());

        meta = pRes.getMetadata();
        assertNull(meta.getNextCursor());
        assertFalse(meta.hasMoreData());
        assertNull(meta.getTotalCount());
    }
}