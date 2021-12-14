package models.files;

import com.google.gson.GsonBuilder;
import com.mpatric.mp3agic.ID3Wrapper;
import com.mpatric.mp3agic.Mp3File;
import models.Model;
import utils.Conv;
import utils.StrictEnumTypeAdapterFactory;

import java.util.List;

public class MusicFile extends Model {
    private Long id;
    private String absolutePath;
    private Long size;
    private Integer bitrate;
    private BitRateType bitRateType;
    private Integer duration;
    private String artist;
    private String album;
    private Short year;
    private String genre;
    private String title;
    private Byte[] albumImage;
    private String albumImageMimeType;

    public MusicFile() {
        this.requiredFields = List.of("absolutePath");
    }

    public MusicFile(Mp3File audioFile) {
        // Note: the absolute path is set by ScanTask, since Mp3File just return what
        // Path#toString returns (which may be a relative path).
        this.setBitRateType(audioFile.isVbr() ? BitRateType.VBR : BitRateType.CBR).
          setBitrate(audioFile.getBitrate()).
          setSize(audioFile.getLength()).
          setDuration((int) audioFile.getLengthInSeconds());

        ID3Wrapper wrapper = new ID3Wrapper(audioFile.getId3v1Tag(), audioFile.getId3v2Tag());

        byte[] albumImage = wrapper.getAlbumImage();

        if (albumImage != null) {
           setAlbumImageMimeType(wrapper.getAlbumImageMimeType()).
           setAlbumImage(albumImage);
        }
    }

    public boolean isValid() {
        if (!super.isValid()) {
            // error types are already configured in the parent class.
            return false;
        }

        this.lengthValidator("absolutePath", this.absolutePath, 1000);
        this.lengthValidator("artist", this.artist, 100);
        this.lengthValidator("album", this.album, 100);
        this.lengthValidator("albumImageMimeType", this.albumImageMimeType, 20);
        this.positiveNumberValidator("duration", this.duration);
        this.positiveNumberValidator("bitrate", this.bitrate);
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

    public Integer getBitrate() {
        return bitrate;
    }

    public MusicFile setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
        return this;
    }

    public BitRateType getBitRateType() {
        return bitRateType;
    }

    public MusicFile setBitRateType(BitRateType bitRateType) {
        this.bitRateType = bitRateType;
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

    public Byte[] getAlbumImage() {
        return albumImage;
    }

    public MusicFile setAlbumImage(Byte[] albumImage) {
        this.albumImage = albumImage;
        return this;
    }

    public MusicFile setAlbumImage(byte[] albumImage) {
        this.albumImage = Conv.byteToByte(albumImage);
        return this;
    }

    public String getAlbumImageMimeType() {
        return albumImageMimeType;
    }

    public MusicFile setAlbumImageMimeType(String albumImageMimeType) {
        this.albumImageMimeType = albumImageMimeType;
        return this;
    }

    public static MusicFile fromJson(String json) {
        return new GsonBuilder()
          .registerTypeAdapterFactory(new StrictEnumTypeAdapterFactory())
          .create().fromJson(json, MusicFile.class);
    }
}