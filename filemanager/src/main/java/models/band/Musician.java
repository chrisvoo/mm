package models.band;

import models.Model;

public class Musician extends Model<Musician> {
    private Long id;
    private String firstName;
    private String lastName;
    private String instruments;
    private Short age;
    private Boolean died;

    public static String tableName() {
        return "musicians";
    }

    public Long getId() {
        return id;
    }

    public Musician setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Musician setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Musician setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getInstruments() {
        return instruments;
    }

    public Musician setInstruments(String instruments) {
        this.instruments = instruments;
        return this;
    }

    public Short getAge() {
        return age;
    }

    public Musician setAge(Short age) {
        this.age = age;
        return this;
    }

    public Boolean isDied() {
        return died;
    }

    public Musician setDied(Boolean died) {
        this.died = died;
        return this;
    }
}