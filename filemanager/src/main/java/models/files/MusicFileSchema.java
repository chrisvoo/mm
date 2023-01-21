package models.files;

import com.google.inject.Inject;
import models.Schema;
import utils.logging.LoggerInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MusicFileSchema extends Schema<MusicFile> {
    public static final String ID = "id";
    public static final String ABSOLUTE_PATH = "absolute_path";
    public static final String SIZE = "size";
    public static final String BITRATE = "bitrate";
    public static final String BITRATE_TYPE = "bitrate_type";
    public static final String DURATION = "duration";
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String YEAR = "year";
    public static final String GENRE = "genre";
    public static final String TITLE = "title";
    public static final String ALBUM_IMAGE = "album_image";
    public static final String ALBUM_IMAGE_MIME_TYPE = "album_image_mime_type";

    public static final String TABLE_NAME = "music_files";

    @Inject public MusicFileSchema(LoggerInterface logger) {
        this.fields = List.of(
          MusicFileSchema.ABSOLUTE_PATH, MusicFileSchema.SIZE, MusicFileSchema.BITRATE,
          MusicFileSchema.BITRATE_TYPE, MusicFileSchema.DURATION, MusicFileSchema.ARTIST,
          MusicFileSchema.ALBUM, MusicFileSchema.YEAR, MusicFileSchema.GENRE,
          MusicFileSchema.TITLE, MusicFileSchema.ALBUM_IMAGE, MusicFileSchema.ALBUM_IMAGE_MIME_TYPE
        );
        this.primaryKeys = List.of(MusicFileSchema.ID);
        this.sortableFields = List.of(
          MusicFileSchema.ARTIST, MusicFileSchema.TITLE, MusicFileSchema.ABSOLUTE_PATH, MusicFileSchema.ID
        );
    }

    public String tableName() {
        return TABLE_NAME;
    }

    public MusicFile getModelFromResultSet(ResultSet rs) throws SQLException {
        MusicFile file = new MusicFile()
            .setId(this.getLong(rs, MusicFileSchema.ID))
            .setAbsolutePath(rs.getString(MusicFileSchema.ABSOLUTE_PATH))
            .setSize(this.getLong(rs, MusicFileSchema.SIZE))
            .setBitrate(this.getInt(rs, MusicFileSchema.BITRATE));

            String bitrateType = rs.getString(MusicFileSchema.BITRATE_TYPE);
            file.setBitRateType(bitrateType != null ? BitRateType.valueOf(bitrateType) : null);

            file.setDuration(this.getInt(rs, MusicFileSchema.DURATION))
            .setArtist(rs.getString(MusicFileSchema.ARTIST))
            .setAlbum(rs.getString(MusicFileSchema.ALBUM))
            .setYear(this.getShort(rs, MusicFileSchema.YEAR))
            .setGenre(rs.getString(MusicFileSchema.GENRE))
            .setTitle(rs.getString(MusicFileSchema.TITLE))
            .setAlbumImage(this.getBytes(rs, MusicFileSchema.ALBUM_IMAGE))
            .setAlbumImageMimeType(rs.getString(MusicFileSchema.ALBUM_IMAGE_MIME_TYPE));

        return file;
    }

    private int setSharedStatementValues(PreparedStatement stmt, MusicFile instance) throws SQLException {
        return this.setSharedStatementValues(stmt, instance, 0);
    }

    private String safeText(String text, int length) {
        return text != null && text.length() > length
               ? text.substring(0, length - 1)
               : text;
    }

    private int setSharedStatementValues(PreparedStatement stmt, MusicFile instance, int indexNum) throws SQLException {
        int index = indexNum;
        stmt.setString(++index, instance.getAbsolutePath());
        this.setLong(stmt, instance.getSize(), ++index);
        this.setInt(stmt, instance.getBitrate(), ++index);
        stmt.setString(++index, instance.getBitRateType() != null ? instance.getBitRateType().name() : null);

        // extra check on the length. For some reason, some corrupted mp3 may result in an over-range duration
        this.setInt(stmt, instance.getDuration() != null
          ? (instance.getDuration() > (3600 * 2)
          ? 0 : instance.getDuration()) : 0, ++index);
        stmt.setString(++index, this.safeText(instance.getArtist(), 100));
        stmt.setString(++index, this.safeText(instance.getAlbum(), 100));

        this.setShort(stmt, instance.getYear(), ++index);
        stmt.setString(++index, instance.getGenre());

        // extra check on the length
        stmt.setString(++index, this.safeText(instance.getTitle(), 100));

        this.setBytes(stmt, instance.getAlbumImage(), ++index);
        stmt.setString(++index, instance.getAlbumImageMimeType());
        return index;
    }

    public void setStatementValues(PreparedStatement stmt, MusicFile instance) throws SQLException {
        int index = this.setSharedStatementValues(stmt, instance);

        if (instance.getId() != null) {
            stmt.setLong(++index, instance.getId());
        }
    }

    public void setStatementValuesForUpsert(PreparedStatement stmt, MusicFile instance) throws SQLException {
        int index = this.setSharedStatementValues(stmt, instance);
        this.setSharedStatementValues(stmt, instance, index);
    }

    /**
     * Particular case useful just for this class. Bulk save of music files.
     * @param stmt The prepared statement.
     * @param files A list of files to be saved.
     */
    public void setStatementValuesForBatch(PreparedStatement stmt, List<MusicFile> files) throws SQLException {
        for (MusicFile instance: files) {
           try {
               int index = this.setSharedStatementValues(stmt, instance);
               this.setSharedStatementValues(stmt, instance, index);
               stmt.addBatch();
           } catch (Exception e) {
               logger.severe("Error setting values for batch. File: " + instance.getAbsolutePath());
           }
        }
    }
}