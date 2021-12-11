package models;

import models.band.Band;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BandTest {
    @Test
    public void validTest() {
        Band band = new Band();
        assertFalse(band.isValid());

        band.setName(" ");
        assertFalse(band.isValid());

        band.setName("AC/DC");
        assertTrue(band.isValid());
    }

    @Test
    public void fromJsonTest() {
        String json = """
            {
                "id": 5,
                "name": "AC/DC",
                "country": "AU",
                "countryName": "Australia",
                "activeFrom": 1973,
                "totalAlbumsReleased": 18,
                "website": "",
                "twitter": ""
            }       
        """;
        Band band = Band.fromJson(json);
        assertEquals(5, band.getId());
        assertEquals("AC/DC", band.getName());
        assertEquals("AU", band.getCountry());
        assertEquals("Australia", band.getCountryName());
        assertEquals(1973, band.getActiveFrom().shortValue());
        assertEquals(18, band.getTotalAlbumsReleased().shortValue());
        assertEquals("", band.getWebsite());
        assertEquals("", band.getTwitter());
    }
}