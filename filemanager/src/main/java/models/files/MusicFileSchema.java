package models.files;

import models.Schema;

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

    public MusicFileSchema() {
        this.fields = List.of(
          MusicFileSchema.ABSOLUTE_PATH, MusicFileSchema.SIZE, MusicFileSchema.BITRATE,
          MusicFileSchema.BITRATE_TYPE, MusicFileSchema.DURATION, MusicFileSchema.ARTIST,
          MusicFileSchema.ALBUM, MusicFileSchema.YEAR, MusicFileSchema.GENRE,
          MusicFileSchema.TITLE, MusicFileSchema.ALBUM_IMAGE, MusicFileSchema.ALBUM_IMAGE_MIME_TYPE
        );
        this.primaryKeys = List.of(MusicFileSchema.ID);
    }

    public String tableName() {
        return "music_files";
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
        int index = 0;
        stmt.setString(++index, instance.getAbsolutePath());
        this.setLong(stmt, instance.getSize(), ++index);
        this.setInt(stmt, instance.getBitrate(), ++index);
        stmt.setString(++index, instance.getBitRateType() != null ? instance.getBitRateType().name() : null);
        this.setInt(stmt, instance.getDuration(), ++index);
        stmt.setString(++index, instance.getArtist());
        stmt.setString(++index, instance.getAlbum());
        this.setShort(stmt, instance.getYear(), ++index);
        stmt.setString(++index, instance.getGenre());
        stmt.setString(++index, instance.getTitle());
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

    /**
     * Particular case useful just for this class. Bulk save of music files.
     * @param stmt The prepared statement.
     * @param files A list of files to be saved.
     */
    public void setStatementValuesForBatch(PreparedStatement stmt, List<MusicFile> files) throws SQLException {
        for (MusicFile instance: files) {
           int index = this.setSharedStatementValues(stmt, instance);
           stmt.setString(++index, instance.getAbsolutePath());
           stmt.addBatch();
        }
    }
}