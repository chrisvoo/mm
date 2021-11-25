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

    /**
     * MySQL connection string
     */
    private String connectionString;

    /**
     * MySQL user
     */
    private String mysqlUser;

    /**
     * MySql pass
     */
    private String mysqlPass;

    private Optional<Integer> tryParseInteger(String string) {
        try {
            return Optional.of(Integer.valueOf(string));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private String getStringEnv(String envName) {
        return Optional.ofNullable(
                System.getenv(envName)
        ).orElseThrow(() -> new EnvException(envName));
    }

    private int getIntEnv(String envName) {
        return this.tryParseInteger(
                System.getenv(envName)
        ).orElseThrow(() -> new EnvException(envName));
    }

    public void loadEnvVars() {
        this.port = this.getIntEnv("FILE_MANAGER_PORT");
        this.musicDirectory = this.getStringEnv("MUSIC_DIRECTORY");

        String mysqlHost = this.getStringEnv("MYSQL_HOST");
        String mysqlDb = this.getStringEnv("MYSQL_DATABASE");
        this.mysqlUser = this.getStringEnv("MYSQL_USER");
        this.mysqlPass = this.getStringEnv("MYSQL_PASSWORD");
        int mysqlPort = this.getIntEnv("MYSQL_PORT");
        this.connectionString = String.format("jdbc:mysql://%s:%d/%s", mysqlHost, mysqlPort, mysqlDb);
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

    public EnvVars setPort(int port) {
        this.port = port;
        return this;
    }

    public String getMusicDirectory() {
        return musicDirectory;
    }

    public EnvVars setMusicDirectory(String musicDirectory) {
        this.musicDirectory = musicDirectory;
        return this;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public EnvVars setConnectionString(String connectionString) {
        this.connectionString = connectionString;
        return this;
    }

    public String getMysqlUser() {
        return mysqlUser;
    }

    public EnvVars setMysqlUser(String mysqlUser) {
        this.mysqlUser = mysqlUser;
        return this;
    }

    public String getMysqlPass() {
        return mysqlPass;
    }

    public EnvVars setMysqlPass(String mysqlPass) {
        this.mysqlPass = mysqlPass;
        return this;
    }
}
