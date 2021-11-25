package utils;

import com.google.inject.AbstractModule;
import exceptions.EnvException;
import repos.BandRepo;
import repos.MusicFileRepo;
import repos.ScannerRepo;
import services.BandService;
import services.MusicFileService;
import services.ScannerService;
import spark.Spark;

public class RoutesModule extends AbstractModule {
    @Override
    protected void configure() {
        try {
            EnvVars envVars = new EnvVars();
            envVars.loadEnvVars();
            bind(EnvVars.class).toInstance(envVars);
        } catch (EnvException e) {
            Spark.stop();
            Spark.awaitStop();

            System.err.println(e.getMessage());
            System.exit(100);
        }

        bind(MusicFileService.class).to(MusicFileRepo.class);
        bind(ScannerService.class).to(ScannerRepo.class);
        bind(BandService.class).to(BandRepo.class);
    }
}