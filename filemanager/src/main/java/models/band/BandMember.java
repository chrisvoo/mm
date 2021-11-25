package models.band;

import java.sql.Date;

public class BandMember {
    private long bandId;
    private long musicianId;
    private Date activeFrom;
    private Date activeTo;

    public static String tableName() {
        return "band_members";
    }

    public long getBandId() {
        return bandId;
    }

    public BandMember setBandId(long bandId) {
        this.bandId = bandId;
        return this;
    }

    public long getMusicianId() {
        return musicianId;
    }

    public BandMember setMusicianId(long musicianId) {
        this.musicianId = musicianId;
        return this;
    }

    public Date getActiveFrom() {
        return activeFrom;
    }

    public BandMember setActiveFrom(Date activeFrom) {
        this.activeFrom = activeFrom;
        return this;
    }

    public Date getActiveTo() {
        return activeTo;
    }

    public BandMember setActiveTo(Date activeTo) {
        this.activeTo = activeTo;
        return this;
    }
}
