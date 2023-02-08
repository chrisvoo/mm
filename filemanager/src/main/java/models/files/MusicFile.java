package models.files;

import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import models.Model;
import utils.logging.LoggerInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MusicFile extends Model<MusicFile> {
    @Inject private static LoggerInterface logger;

    private Long id;
    private String absolutePath;
    private Long size;
    private Integer duration;
    private String artist;
    private String album;
    private Short year;
    private String genre;
    private String title;

    public MusicFile() {
        this.requiredFields = List.of("absolutePath");
    }

    public boolean isValid() {
        if (!super.isValid()) {
            // error types are already configured in the parent class.
            return false;
        }

        this.lengthValidator("absolutePath", this.absolutePath, 1000);
        this.lengthValidator("artist", this.artist, 100);
        this.lengthValidator("album", this.album, 100);
        this.positiveNumberValidator("duration", this.duration != null ? (long)this.duration : null);
        this.positiveNumberValidator("year", this.year != null ? (long)this.year : null);

        return this.errorCode == null;
    }

    public Long getId() {
        return id;
    }

    public MusicFile setId(Long id) {
        this.id = id;
        return this;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public MusicFile setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public MusicFile setSize(Long size) {
        this.size = size;
        return this;
    }

    public Integer getDuration() {
        return duration;
    }

    public MusicFile setDuration(Integer duration) {
        this.duration = duration;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public MusicFile setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public MusicFile setAlbum(String album) {
        this.album = album;
        return this;
    }

    public Short getYear() {
        return year;
    }

    public MusicFile setYear(Short year) {
        this.year = year;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public MusicFile setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MusicFile setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * In case the metadata aren't readable, call this method to get the file's size.
     * @param p The Path.
     */
    public MusicFile calculateSize(Path p) {
        try {
            this.size = Files.size(p);
            if (this.size <= 0) {
                logger.severe(String.format("Negative size for %s", p));
            }
        } catch (IOException e) {
            logger.severe(
              "Cannot read file's size for " +
                p.toFile().getName() + ": " + e.getMessage()
            );
            this.size = 0L;
        }
        return this;
    }

    public static MusicFile fromJson(String json) {
        return new GsonBuilder()
//          .registerTypeAdapterFactory(new StrictEnumTypeAdapterFactory())
          .create().fromJson(json, MusicFile.class);
    }

    public File toFile() {
        return new File(this.absolutePath);
    }
}