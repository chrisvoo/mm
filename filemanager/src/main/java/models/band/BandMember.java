package models.band;

import com.google.gson.Gson;
import models.Model;

import java.sql.Date;
import java.util.List;

public class BandMember extends Model {
    private Long bandId;
    private Long musicianId;
    private Date activeFrom;
    private Date activeTo;

    public BandMember() {
        this.requiredFields = List.of("bandId", "musicianId");
    }

    public static String tableName() {
        return "band_members";
    }

    public Long getBandId() {
        return bandId;
    }

    public BandMember setBandId(Long bandId) {
        this.bandId = bandId;
        return this;
    }

    public Long getMusicianId() {
        return musicianId;
    }

    public BandMember setMusicianId(Long musicianId) {
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

    public static BandMember fromJson(String json) {
        return new Gson().fromJson(json, BandMember.class);
    }
}