package models;

import com.google.gson.Gson;
import models.playlist.Playlist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PlaylistTest {
  @Test
  public void requiredFieldsTest() {
    Playlist play = new Playlist();
    Assertions.assertFalse(play.isValid());
  }

  private Timestamp getCurTime(String dateTime) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date parsedDate = dateFormat.parse(dateTime);
    return new Timestamp(parsedDate.getTime());
  }

  @Test
  public void fromJsonTest(TestReporter rep) throws ParseException {
    Playlist play = new Playlist()
      .setId(5L)
      .setCreatedAt(this.getCurTime("2022-08-19 15:45:32"))
      .setName("Test playlist")
      .setRating(5)
      .setTags(List.of("metal", "fun"));

    String json = """
        {
           "id": 5,
           "name": "Test playlist",
           "created_at": "%s",
           "image": null,
           "rating": 5,
           "tags": ["metal", "fun"]
        }
      """.formatted("2022-08-19 15:45:32");

//    rep.publishEntry(json);
    Playlist result = new Gson().fromJson(json, Playlist.class);
    assertEquals(play.getId(), result.getId());
    assertNull(result.getImage());
    assertEquals(play.getName(), result.getName());
    assertEquals(play.getRating(), result.getRating());
    assertEquals(play.getTags(), result.getTags());
  }
}