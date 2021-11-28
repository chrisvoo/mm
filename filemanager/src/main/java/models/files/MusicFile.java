package models.files;

import models.Model;

import java.sql.Date;

public class MusicFile extends Model {
    private Long id;
    private String absolutePath;
    private Long size;
    private Integer bitrate;
    private BitRateType bitRateType;
    private Integer duration;
    private String artist;
    private String album;
    private Date year;
    private String genre;
    private String title;
    private Byte[] albumImage;
    private String albumImageMimeType;

    public static String tableName() {
        return "music_files";
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

    public Date getYear() {
        return year;
    }

    public MusicFile setYear(Date year) {
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

    public String getAlbumImageMimeType() {
        return albumImageMimeType;
    }

    public MusicFile setAlbumImageMimeType(String albumImageMimeType) {
        this.albumImageMimeType = albumImageMimeType;
        return this;
    }
}
