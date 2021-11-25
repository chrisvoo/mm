package models.band;

public class Musician {
    private long id;
    private String firstName;
    private String lastName;
    private String instruments;
    private short age;
    private boolean died;

    public static String tableName() {
        return "musicians";
    }

    public long getId() {
        return id;
    }

    public Musician setId(long id) {
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

    public short getAge() {
        return age;
    }

    public Musician setAge(short age) {
        this.age = age;
        return this;
    }

    public boolean isDied() {
        return died;
    }

    public Musician setDied(boolean died) {
        this.died = died;
        return this;
    }
}
