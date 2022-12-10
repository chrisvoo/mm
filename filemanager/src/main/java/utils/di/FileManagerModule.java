package utils.di;

import com.google.inject.AbstractModule;
import exceptions.FileManagerException;
import models.files.MusicFile;
import repos.BandRepo;
import repos.MusicFileRepo;
import repos.ScannerRepo;
import repos.StatsRepo;
import services.BandService;
import services.MusicFileService;
import services.ScannerService;
import services.StatsService;
import spark.Spark;
import utils.Db;
import utils.EnvVars;
import utils.FileUtils;
import utils.logging.LoggerFactory;
import utils.logging.LoggerInterface;
import watcher.Watcher;

import java.io.IOException;

import static org.slf4j.simple.SimpleLogger.DEFAULT_LOG_LEVEL_KEY;

public class FileManagerModule extends AbstractModule {
    protected EnvVars envVars;

    @Override
    protected void configure() {
        Watcher watcher = null;
        LoggerFactory loggerFactory = null;
        try {
            this.envVars = new EnvVars();
            this.envVars.loadEnvVars();

            // used by Jetty
            System.setProperty(DEFAULT_LOG_LEVEL_KEY, "WARN");

            bind(EnvVars.class).toInstance(this.envVars);

            loggerFactory = new LoggerFactory(envVars);
            bind(LoggerInterface.class).toInstance(loggerFactory);

            Db db = new Db(this.envVars);
            bind(Db.class).toInstance(db);

            watcher = new Watcher();
            bind(Watcher.class).toInstance(watcher);
        } catch (FileManagerException | IOException e) {
            Spark.stop();
            Spark.awaitStop();

            if (watcher != null) {
                watcher.close();
            }

            System.err.println(e.getMessage());
            System.exit(
              e instanceof FileManagerException
                ? ((FileManagerException) e).getCode()
                : 500
            );
        }

        // classes using logger inside static methods need to be added here
        requestStaticInjection(MusicFile.class, FileUtils.class);

        bind(MusicFileService.class).to(MusicFileRepo.class);
        bind(ScannerService.class).to(ScannerRepo.class);
        bind(BandService.class).to(BandRepo.class);
        bind(StatsService.class).to(StatsRepo.class);
    }
}