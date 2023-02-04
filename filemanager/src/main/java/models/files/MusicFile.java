package models.files;

import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.mpatric.mp3agic.*;
import models.Model;
import utils.gson.StrictEnumTypeAdapterFactory;
import utils.logging.LoggerInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static MusicFile fromPath(Path resource) {
        MusicFile audioFile;
        try {
            // If for some reasons, metadata aren't readable, we just store the file path
            audioFile = new MusicFile(resource);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            String filePath = resource.normalize().toAbsolutePath().toString();
            audioFile = new MusicFile()
              .setAbsolutePath(filePath)
              .calculateSize(resource);
        }

        return audioFile;
    }

    public MusicFile() {
        this.requiredFields = List.of("absolutePath");
    }

    /**
     * This initializes all the class properties parsing the MP3 file metadata
     * @param audioFile an instance of this class.
     * @deprecated in favor of EyeD3
     */
    public MusicFile(Mp3File audioFile) {
        // Note: the absolute path is set by ScanTask, since Mp3File just return what
        // Path#toString returns (which may be a relative path).
        this.setSize(audioFile.getLength()).
          setDuration((int) audioFile.getLengthInSeconds()).
          setAbsolutePath(audioFile.getFilename());

        ID3Wrapper wrapper = new ID3Wrapper(audioFile.getId3v1Tag(), audioFile.getId3v2Tag());

        setGenre(wrapper.getGenreDescription());

        if (wrapper.getArtist() != null && !wrapper.getArtist().isBlank()) {
            setArtist(wrapper.getArtist().trim());
        } else if (wrapper.getAlbumArtist() != null && !wrapper.getAlbumArtist().isBlank()) {
            setArtist(wrapper.getAlbumArtist().trim());
        }

        setTitle(wrapper.getTitle()).
        setAlbum(wrapper.getAlbum());

        String year = wrapper.getYear();
        try {
            if (year == null || year.trim().isBlank()) {
                if (audioFile.hasId3v2Tag()) {
                    AbstractID3v2Tag tag = ID3v2TagFactory.createTag(
                      Files.readAllBytes(
                        Paths.get(audioFile.getFilename())
                      )
                    );

                    if (tag instanceof ID3v24Tag theTag) {
                        year = theTag.getRecordingTime();
                    }
                }
            }

            if (year != null && year.matches("\\d{4}(-\\d{2}-\\d{2}(\\s\\d{2}:\\d{2}:\\d{2})?)?")) {
                setYear(Short.parseShort(year.substring(0, 4)));
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    /**
     * @deprecated in favor of EyeD3
     * @param resource
     * @throws InvalidDataException
     * @throws UnsupportedTagException
     * @throws IOException
     */
    public MusicFile(Path resource) throws InvalidDataException, UnsupportedTagException, IOException {
        this(new Mp3File(resource));
    }

    public boolean isValid() {
        if (!super.isValid()) {
            // error types are already configured in the parent class.
            return false;
        }

        this.lengthValidator("absolutePath", this.absolutePath, 1000);
        this.lengthValidator("artist", this.artist, 100);
        this.lengthValidator("album", this.album, 100);
        this.positiveNumberValidator("duration", this.duration);
        this.positiveNumberValidator("year", this.year);

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
          .registerTypeAdapterFactory(new StrictEnumTypeAdapterFactory())
          .create().fromJson(json, MusicFile.class);
    }

    public File toFile() {
        return new File(this.absolutePath);
    }
}