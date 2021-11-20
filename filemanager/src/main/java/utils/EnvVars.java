package utils;

import exceptions.EnvException;

import java.util.Optional;

public class EnvVars {
    /**
     * Spark port
     */
    private int port;

    /**
     * Main directory containing all your music
     */
    private String musicDirectory;

    private Optional<Integer> tryParseInteger(String string) {
        try {
            return Optional.of(Integer.valueOf(string));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private String getStringEnv(String envName) {
        return Optional.of(
                System.getenv(envName)
        ).orElseThrow(() -> new EnvException(envName));
    }

    private int getIntEnv(String envName) {
        return this.tryParseInteger(
                System.getenv(envName)
        ).orElseThrow(() -> new EnvException(envName));
    }

    public EnvVars() {
        this.port = this.getIntEnv("FILE_MANAGER_PORT");
        this.musicDirectory = this.getStringEnv("MUSIC_DIRECTORY");

        String mysqlHost = this.getStringEnv("MYSQL_HOST");
    }

    public static boolean isJUnitTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

    public int getPort() {
        return port;
    }
}
