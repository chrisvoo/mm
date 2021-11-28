package repos;

import com.google.inject.Inject;
import exceptions.DbException;
import models.band.Band;
import services.BandService;
import utils.Db;

import java.sql.*;
import java.util.logging.Logger;

public class BandRepo implements BandService {
    private static final Logger logger = Logger.getLogger(BandRepo.class.getName());
    @Inject private Db db;

    /**
     * Return a band by its primary key
     *
     * @param id Band's primary key
     * @return Band
     */
    @Override
    public Band getById(long id) {
        String sql = String.format(
            "SELECT * FROM %s WHERE id = ?", Band.tableName()
        );
        try (
                Connection conn = db.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Band()
                        .setId(rs.getLong("id"))
                        .setName(rs.getString("name"))
                        .setCountry(rs.getString("country"))
                        .setCountryName(rs.getString("country_name"))
                        .setActiveFrom(rs.getDate("active_from"))
                        .setActiveTo(rs.getDate("active_to"))
                        .setTotalAlbumsReleased(rs.getShort("total_albums_released"))
                        .setWebsite(rs.getString("website"))
                        .setTwitter(rs.getString("twitter"));
                }

                return null;
            }
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            throw new DbException("Cannot get the band", DbException.SQL_EXCEPTION);
        }
    }

    /**
     * Upsert of a band into the database
     *
     * @param band The band.
     * @return The band
     */
    @Override
    public Band save(Band band) {
        return null;
    }
}
