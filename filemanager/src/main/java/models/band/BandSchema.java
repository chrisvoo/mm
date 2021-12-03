package models.band;

import models.Schema;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BandSchema extends Schema<Band> {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String COUNTRY = "country";
    public static final String COUNTRY_NAME = "country_name";
    public static final String ACTIVE_FROM = "active_from";
    public static final String ACTIVE_TO = "active_to";
    public static final String TOTAL_ALBUM_RELEASED = "total_albums_released";
    public static final String WEBSITE = "website";
    public static final String TWITTER = "twitter";

    public BandSchema() {
        this.fieldsWithoutPK = List.of(
            BandSchema.NAME, BandSchema.COUNTRY, BandSchema.COUNTRY_NAME,
            BandSchema.ACTIVE_FROM, BandSchema.ACTIVE_TO, BandSchema.TOTAL_ALBUM_RELEASED,
            BandSchema.WEBSITE, BandSchema.TWITTER
        );
    }

    public String tableName() {
        return "bands";
    }

    public Band getModelFromResultSet(ResultSet rs) throws SQLException {
        return new Band()
                .setId(rs.getLong(BandSchema.ID))
                .setName(rs.getString(BandSchema.NAME))
                .setCountry(rs.getString(BandSchema.COUNTRY))
                .setCountryName(rs.getString(BandSchema.COUNTRY_NAME))
                .setActiveFrom(rs.getDate(BandSchema.ACTIVE_FROM))
                .setActiveTo(rs.getDate(BandSchema.ACTIVE_TO))
                .setTotalAlbumsReleased(rs.getShort(BandSchema.TOTAL_ALBUM_RELEASED))
                .setWebsite(rs.getString(BandSchema.WEBSITE))
                .setTwitter(rs.getString(BandSchema.TWITTER));
    }

    public void setStatementValues(PreparedStatement stmt, Band instance) throws SQLException {
        int index = 0;
        stmt.setString(++index, instance.getName());
        stmt.setString(++index, instance.getCountry());
        stmt.setString(++index, instance.getCountryName());
        stmt.setDate(++index, instance.getActiveFrom());
        stmt.setDate(++index, instance.getActiveTo());
        stmt.setShort(++index, instance.getTotalAlbumsReleased());
        stmt.setString(++index, instance.getWebsite());
        stmt.setString(++index, instance.getTwitter());
    }
}
