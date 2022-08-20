package models.playlist;

import com.google.gson.Gson;
import models.Model;
import models.files.MusicFile;

import java.sql.Timestamp;
import java.util.List;

public class Playlist extends Model<Playlist> {
  private Long id;
  private String name;
  private Byte[] image;
  private Integer rating;
  private List<String> tags;
  private Timestamp createdAt, updatedAt;
  private List<MusicFile> tracks;

  public Playlist() {
    this.requiredFields = List.of("name");
  }

  public Long getId() {
    return id;
  }

  public Playlist setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Playlist setName(String name) {
    this.name = name;
    return this;
  }

  public Byte[] getImage() {
    return image;
  }

  public Playlist setImage(Byte[] image) {
    this.image = image;
    return this;
  }

  public Integer getRating() {
    return rating;
  }

  public Playlist setRating(Integer rating) {
    this.rating = rating;
    return this;
  }

  public List<String> getTags() {
    return tags;
  }

  public Playlist setTags(String jsonTags) {
    this.tags = jsonTags != null && !jsonTags.isBlank()
                ? new Gson().fromJson(jsonTags, List.class)
                : List.of();
    return this;
  }

  public Playlist setTags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public Playlist setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public Timestamp getUpdatedAt() {
    return updatedAt;
  }

  public Playlist setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public List<MusicFile> getTracks() {
    return tracks;
  }

  public Playlist setTracks(List<MusicFile> tracks) {
    this.tracks = tracks;
    return this;
  }
}