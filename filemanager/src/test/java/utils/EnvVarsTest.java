package utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnvVarsTest {
    @Test
    public void envVarsTest() {
        EnvVars ev = new EnvVars();
        ev.loadEnvVars();

        assertEquals("mm_test", ev.getMysqlUser());
        assertNotNull(ev.getMysqlPass());
        assertEquals("jdbc:mysql://127.0.0.1:3307/mm_test", ev.getConnectionString());
    }
}
