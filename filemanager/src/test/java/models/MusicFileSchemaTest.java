package models;

import models.files.MusicFileSchema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicFileSchemaTest {

  @Test
  public void testInsertSQLGeneration() {
    MusicFileSchema schema = new MusicFileSchema();

    List<String> fields = schema.getFields(false);
    List<String> list = new ArrayList<>(Collections.nCopies(fields.size(), "?"));
    String placeholders = String.join(",", list);

    Assertions.assertEquals(
      "INSERT INTO music_files (" + String.join(",", fields) + ") VALUES (" + placeholders + ")",
      schema.getSqlForInsert()
    );
  }

  @Test
  public void testUpdateSQLGeneration() {
    MusicFileSchema schema = new MusicFileSchema();
    String sql = "UPDATE music_files SET absolute_path = ?,size = ?,bitrate = ?,bitrate_type = ?,duration = ?," +
      "artist = ?,album = ?,year = ?,genre = ?,title = ?,album_image = ?,album_image_mime_type = ? WHERE id = ?";
    Assertions.assertEquals(sql, schema.getSqlForUpdate());
  }
}