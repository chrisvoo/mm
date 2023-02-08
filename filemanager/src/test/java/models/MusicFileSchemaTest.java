package models;

import com.google.inject.Inject;
import models.files.MusicFileSchema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static src.TestUtils.inject;

public class MusicFileSchemaTest {

  @Inject private MusicFileSchema schema = inject(MusicFileSchema.class);

  @Test
  public void testInsertSQLGeneration() {
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
    String sql = "UPDATE music_files SET absolute_path = ?,size = ?,duration = ?," +
      "artist = ?,album = ?,year = ?,genre = ?,title = ? WHERE id = ?";
    Assertions.assertEquals(sql, schema.getSqlForUpdate());
  }

  @Test
  public void testUpsertSQLGeneration() {
    List<String> fields = schema.getFields(false);
    List<String> list = new ArrayList<>(Collections.nCopies(fields.size(), "?"));
    String placeholders = String.join(",", list);

    String insert = "INSERT INTO music_files (" + String.join(",", fields) + ") VALUES (" + placeholders + ")";

    String update = "UPDATE absolute_path = ?,size = ?,duration = ?," +
      "artist = ?,album = ?,year = ?,genre = ?,title = ?";

    String sql = insert + " ON DUPLICATE KEY " + update;

    Assertions.assertEquals(sql, schema.getSqlForUpsert());
  }
}