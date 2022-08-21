package models;

import models.band.BandSchema;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static src.TestUtils.inject;

public class BandSchemaTest {

  private final BandSchema schema = inject(BandSchema.class);

  @Test
  public void testInsertSQLGeneration() {
    List<String> fields = schema.getFields(false);
    List<String> list = new ArrayList<>(Collections.nCopies(fields.size(), "?"));
    String placeholders = String.join(",", list);

    assertEquals(
      "INSERT INTO bands (" + String.join(",", fields) + ") VALUES (" + placeholders + ")",
      schema.getSqlForInsert()
    );
  }

  @Test
  public void testUpdateSQLGeneration() {
//    BandSchema schema = new BandSchema();
    String sql = "UPDATE bands SET name = ?,country = ?,country_name = ?,active_from = ?,active_to = ?," +
      "total_albums_released = ?,website = ?,twitter = ? WHERE id = ?";
    assertEquals(sql, schema.getSqlForUpdate());
  }

  @Test
  public void testDeleteSQLGeneration() {
//    BandSchema schema = new BandSchema();
    String sql = "DELETE FROM bands WHERE id = ?";
    assertEquals(sql, schema.getSqlForDelete());
  }
}