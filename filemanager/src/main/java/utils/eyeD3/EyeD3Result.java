package utils.eyeD3;

import models.files.MusicFile;

public class EyeD3Result {
  public String path;
  public EyeD3Info info;
  public String album;
  public String artist;
  public String album_artist;
  public String best_release_date;
  public String composer;
  public String genre;
  public String non_std_genre;
  public String original_release_date;
  public String recording_date;
  public String title;

  public boolean hasInvalidValues() {
    if (info != null) {
      return info.size_bytes <= 0 || info.time_secs <= 0;
    }

    return false;
  }

  public MusicFile toMusicFile() {
    MusicFile file = new MusicFile()
      .setAlbum(album)
      .setAbsolutePath(path)
      .setDuration(info != null ? (int)info.time_secs : 0)
      .setSize(info != null ? info.size_bytes : 0)
      .setGenre(genre != null ? genre : non_std_genre)
      .setArtist(artist != null ? artist : album_artist)
      .setTitle(title);

    if (original_release_date != null && original_release_date.matches("\\d{4}(-\\d{2}(-\\d{2})?(\\s\\d{2}:\\d{2}:\\d{2})?)?")) {
      file.setYear(Short.parseShort(original_release_date.substring(0, 4)));
    } else if (recording_date != null && recording_date.matches("\\d{4}(-\\d{2}-(-\\d{2})?(\\s\\d{2}:\\d{2}:\\d{2})?)?")) {
      file.setYear(Short.parseShort(recording_date.substring(0, 4)));
    }

    return file;
  }
}