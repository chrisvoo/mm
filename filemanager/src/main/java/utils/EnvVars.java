package utils;

import exceptions.EnvException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class EnvVars {
    /**
     * Spark port
     */
    private int port;

    private Level logLevel;

    /**
     * Main directory containing all your music
     */
    private Path musicDirectory;

    /**
     * MySQL's connection string
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

    private Environments environment;

    private static final List<String> standardLevels = List.of(
      "OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST", "ALL"
    );

    public enum Environments {
        DEVELOPMENT,
        TEST,
        PRODUCTION
    }

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
        this.musicDirectory = Paths.get(this.getStringEnv("MUSIC_DIRECTORY"));
        this.environment = Environments.valueOf(this.getStringEnv("ENV"));

        if (!Files.isDirectory(this.musicDirectory)) {
            throw new EnvException("The specified music directory is not a directory!", EnvException.MUSIC_DIR_INVALID);
        }

        if (!Files.exists(this.musicDirectory)) {
            throw new EnvException("The specified music directory does not exist!", EnvException.MUSIC_DIR_INVALID);
        }

        String mysqlHost = this.getStringEnv("MYSQL_HOST");
        String mysqlDb = this.getStringEnv("MYSQL_DATABASE");
        this.mysqlUser = this.getStringEnv("MYSQL_USER");
        this.mysqlPass = this.getStringEnv("MYSQL_PASSWORD");
        int mysqlPort = this.getIntEnv("MYSQL_PORT");
        this.connectionString = String.format("jdbc:mysql://%s:%d/%s", mysqlHost, mysqlPort, mysqlDb);

        // logging
        String logLevel = this.getStringEnv("LOGS_LEVEL");
        if (EnvVars.standardLevels.contains(logLevel)) {
            this.logLevel = Level.parse(logLevel);
        } else {
            throw new EnvException("The specified log level is wrong. Accepted valus: " +
              String.join(",", EnvVars.standardLevels), EnvException.ENV_ERROR);
        }
    }

    public Environments getEnvironment() {
        return environment;
    }

    public EnvVars setEnvironment(Environments environment) {
        this.environment = environment;
        return this;
    }

    public int getPort() {
        return port;
    }

    public EnvVars setPort(int port) {
        this.port = port;
        return this;
    }

    public Path getMusicDirectory() {
        return musicDirectory;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public EnvVars setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    public EnvVars setMusicDirectory(Path musicDirectory) {
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