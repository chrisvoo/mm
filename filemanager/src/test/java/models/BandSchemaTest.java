package models;

import models.band.BandSchema;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BandSchemaTest {

  @Test
  public void testInsertSQLGeneration() {
    BandSchema schema = new BandSchema();

    List<String> fields = schema.getFields(false);
    List<String> list = new ArrayList<>(Collections.nCopies(fields.size(), "?"));
    String placeholders = String.join(",", list);

    Assertions.assertEquals(
      "INSERT INTO bands (" + String.join(",", fields) + ") VALUES (" + placeholders + ")",
      schema.getSqlForInsert()
    );
  }

  @Test
  public void testUpdateSQLGeneration() {
    BandSchema schema = new BandSchema();
    String sql = "UPDATE bands SET name = ?,country = ?,country_name = ?,active_from = ?,active_to = ?," +
      "total_albums_released = ?,website = ?,twitter = ? WHERE id = ?";
    Assertions.assertEquals(sql, schema.getSqlForUpdate());
  }
}