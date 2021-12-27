package models.band;

import models.Model;

import java.sql.Date;
import java.util.List;

public class BandActivity extends Model<BandActivity> {
    private Long bandId;
    private Date activeFrom;
    private Date activeTo;

    public BandActivity() {
        this.requiredFields = List.of("bandId");
    }

    public static String tableName() {
        return "bands_activity";
    }

    public Long getBandId() {
        return bandId;
    }

    public BandActivity setBandId(Long bandId) {
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