package models.band;

import models.Model;

import java.sql.Date;
import java.util.List;

public class Band extends Model {

    private Long id;

    private String name;
    /**
     * ISO two letters
     */
    private String country;

    private String countryName;
    /**
     * Active from (in general)
     */
    private Date activeFrom;
    /**
     * Active to
     */
    private Date activeTo;
    private Short totalAlbumsReleased;
    private String website;
    private String twitter;

    /**
     * A band may be inactive for certain periods of time, we list here the hiatus.
     * Transient value
     */
    private List<BandActivity> activities;

    public Band() {
        this.requiredFields = List.of(
          "name"
        );
    }

    public static String tableName() {
        return "bands";
    }

    public Long getId() {
        return id;
    }

    public Band setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Band setName(String name) {
        this.name = name;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Band setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCountryName() {
        return countryName;
    }

    public Band setCountryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public Date getActiveFrom() {
        return activeFrom;
    }

    public Band setActiveFrom(Date activeFrom) {
        this.activeFrom = activeFrom;
        return this;
    }

    public Date getActiveTo() {
        return activeTo;
    }

    public Band setActiveTo(Date activeTo) {
        this.activeTo = activeTo;
        return this;
    }

    public short getTotalAlbumsReleased() {
        return totalAlbumsReleased;
    }

    public Band setTotalAlbumsReleased(short totalAlbumsReleased) {
        this.totalAlbumsReleased = totalAlbumsReleased;
        return this;
    }

    public String getWebsite() {
        return website;
    }

    public Band setWebsite(String website) {
        this.website = website;
        return this;
    }

    public String getTwitter() {
        return twitter;
    }

    public Band setTwitter(String twitter) {
        this.twitter = twitter;
        return this;
    }

    public List<BandActivity> getActivities() {
        return activities;
    }

    public Band setActivities(List<BandActivity> activities) {
        this.activities = activities;
        return this;
    }
}
