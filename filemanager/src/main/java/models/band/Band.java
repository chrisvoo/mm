package models.band;

import com.google.gson.Gson;
import models.Model;

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
    private Short activeFrom;
    /**
     * Active to
     */
    private Short activeTo;
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

    public boolean isValid() {
        if (!super.isValid()) {
            // error types are already configured in the parent class.
            return false;
        }

        this.lengthValidator("name", this.name, 100);
        this.lengthValidator("countryName", this.countryName, 100);
        this.lengthValidator("country", this.country, 2);
        this.lengthValidator("website", this.website, 150);
        this.lengthValidator("twitter", this.twitter, 150);

        return this.errorCode == null;
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

    public Short getActiveFrom() {
        return activeFrom;
    }

    public Band setActiveFrom(Short activeFrom) {
        this.activeFrom = activeFrom;
        return this;
    }

    public Short getActiveTo() {
        return activeTo;
    }

    public Band setActiveTo(Short activeTo) {
        this.activeTo = activeTo;
        return this;
    }

    public Short getTotalAlbumsReleased() {
        return totalAlbumsReleased;
    }

    public Band setTotalAlbumsReleased(Short totalAlbumsReleased) {
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

    public static Band fromJson(String json) {
        return new Gson().fromJson(json, Band.class);
    }
}
