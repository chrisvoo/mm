package utils;

import exceptions.EnvException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
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

    private Properties loadPropertiesFile() {
        final Properties properties = new Properties();
        try {
            String resName = String.format("env.%s.properties", this.environment.name().toLowerCase());
            properties.load(this.getClass().getClassLoader().getResourceAsStream(resName));
            return properties;
        } catch (IOException e) {
            return null;
        }
    }

    private Optional<Integer> tryParseInteger(String string) {
        try {
            return Optional.of(Integer.valueOf(string));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private String getStringEnv(String envName) {
        return this.getStringEnv(envName, false);
    }

    private String getStringEnv(String envName, boolean fromSystem) {
        return Optional.ofNullable(
          fromSystem ? System.getenv(envName) : envName
        ).orElseThrow(() -> new EnvException(envName));
    }

    private int getIntEnv(String envName) {
        return this.getIntEnv(envName, false);
    }

    private int getIntEnv(String envName, boolean fromSystem) {
        return this.tryParseInteger(
          fromSystem ? System.getenv(envName) : envName
        ).orElseThrow(() -> new EnvException(envName));
    }

    private void checkMusicDirectory() {
        if (!Files.isDirectory(this.musicDirectory)) {
            throw new EnvException("The specified music directory is not a directory!", EnvException.MUSIC_DIR_INVALID);
        }

        if (!Files.exists(this.musicDirectory)) {
            throw new EnvException("The specified music directory does not exist!", EnvException.MUSIC_DIR_INVALID);
        }
    }

    private void initLogLevel(String logLevel) {
        if (EnvVars.standardLevels.contains(logLevel)) {
            this.logLevel = Level.parse(logLevel);
        } else {
            throw new EnvException("The specified log level is wrong. Accepted valus: " +
              String.join(",", EnvVars.standardLevels), EnvException.ENV_ERROR);
        }
    }

    /**
     * In case there's LOAD_FROM_ENV variable set to true, the values are taken from a properties files in the resources
     * directory, corresponding to the environment selected (test, development, production).
     * This is convenient for when you want to run the app outside the docker environment.
     */
    private void loadFromProperties() {
        Properties props = this.loadPropertiesFile();
        if (props == null) {
            throw new EnvException("Can't read properties file!");
        }

        this.port = this.getIntEnv(props.getProperty("FILE_MANAGER_PORT"));
        this.musicDirectory = Paths.get(this.getStringEnv(props.getProperty("MUSIC_DIRECTORY")));
        this.checkMusicDirectory();

        String mysqlHost = this.getStringEnv(props.getProperty("MYSQL_HOST"));
        String mysqlDb = this.getStringEnv(props.getProperty("MYSQL_DATABASE"));
        this.mysqlUser = this.getStringEnv(props.getProperty("MYSQL_USER"));
        this.mysqlPass = this.getStringEnv(props.getProperty("MYSQL_PASSWORD"));
        int mysqlPort = this.getIntEnv(props.getProperty("MYSQL_PORT"));
        this.connectionString = String.format("jdbc:mysql://%s:%d/%s", mysqlHost, mysqlPort, mysqlDb);

        String logLevel = this.getStringEnv(props.getProperty("LOGS_LEVEL"));
        this.initLogLevel(logLevel);
    }

    public void loadEnvVars() {
        this.environment = Environments.valueOf(this.getStringEnv("ENV", true));

        String loadFromEnv = Optional.ofNullable(System.getenv("LOAD_FROM_ENV")).orElse("false");
        if (loadFromEnv.equalsIgnoreCase("true")) {
            this.loadFromProperties();
            return;
        }

        this.port = this.getIntEnv("FILE_MANAGER_PORT", true);
        this.musicDirectory = Paths.get(this.getStringEnv("MUSIC_DIRECTORY", true));
        this.checkMusicDirectory();

        String mysqlHost = this.getStringEnv("MYSQL_HOST", true);
        String mysqlDb = this.getStringEnv("MYSQL_DATABASE", true);
        this.mysqlUser = this.getStringEnv("MYSQL_USER", true);
        this.mysqlPass = this.getStringEnv("MYSQL_PASSWORD", true);
        int mysqlPort = this.getIntEnv("MYSQL_PORT", true);
        this.connectionString = String.format("jdbc:mysql://%s:%d/%s", mysqlHost, mysqlPort, mysqlDb);

        // logging
        String logLevel = this.getStringEnv("LOGS_LEVEL", true);
        this.initLogLevel(logLevel);
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