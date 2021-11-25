package models.band;

import java.sql.Date;

public class BandActivity {
    private long bandId;
    private Date activeFrom;
    private Date activeTo;

    public static String tableName() {
        return "bands_activity";
    }

    public long getBandId() {
        return bandId;
    }

    public BandActivity setBandId(long bandId) {
        this.bandId = bandId;
        return this;
    }

    public Date getActiveFrom() {
        return activeFrom;
    }

    public BandActivity setActiveFrom(Date activeFrom) {
        this.activeFrom = activeFrom;
        return this;
    }

    public Date getActiveTo() {
        return activeTo;
    }

    public BandActivity setActiveTo(Date activeTo) {
        this.activeTo = activeTo;
        return this;
    }
}
