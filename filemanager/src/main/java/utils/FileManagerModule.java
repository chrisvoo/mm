package utils;

import com.google.inject.AbstractModule;
import exceptions.FileManagerException;
import repos.BandRepo;
import repos.MusicFileRepo;
import repos.ScannerRepo;
import repos.StatsRepo;
import services.BandService;
import services.MusicFileService;
import services.ScannerService;
import services.StatsService;
import spark.Spark;
import watcher.Watcher;

import java.io.IOException;

public class FileManagerModule extends AbstractModule {
    @Override
    protected void configure() {
        Watcher watcher = null;
        try {
            EnvVars envVars = new EnvVars();
            envVars.loadEnvVars();
            bind(EnvVars.class).toInstance(envVars);

            Db db = new Db(envVars);
            bind(Db.class).toInstance(db);

            watcher = new Watcher();
            bind(Watcher.class).toInstance(watcher);
        } catch (FileManagerException | IOException e) {
            Spark.stop();
            Spark.awaitStop();

            if (watcher != null) {
                watcher.stop();
            }

            System.err.println(e.getMessage());
            System.exit(
              e instanceof FileManagerException
                ? ((FileManagerException) e).getCode()
                : 500
            );
        }

        bind(MusicFileService.class).to(MusicFileRepo.class);
        bind(ScannerService.class).to(ScannerRepo.class);
        bind(BandService.class).to(BandRepo.class);
        bind(StatsService.class).to(StatsRepo.class);
    }
}