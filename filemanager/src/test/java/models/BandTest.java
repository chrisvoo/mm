package models;

import models.band.Band;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BandTest {
    @Test
    public void validTest() {
        Band band = new Band();
        Assertions.assertFalse(band.isValid());

        band.setName(" ");
        Assertions.assertFalse(band.isValid());

        band.setName("AC/DC");
        Assertions.assertTrue(band.isValid());
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
        Assertions.assertEquals(5, band.getId());
        Assertions.assertEquals("AC/DC", band.getName());
        Assertions.assertEquals("AU", band.getCountry());
        Assertions.assertEquals("Australia", band.getCountryName());
        Assertions.assertEquals(1973, band.getActiveFrom().shortValue());
        Assertions.assertEquals(18, band.getTotalAlbumsReleased().shortValue());
        Assertions.assertEquals("", band.getWebsite());
        Assertions.assertEquals("", band.getTwitter());
    }
}