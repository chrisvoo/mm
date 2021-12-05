import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.logging.Logger;
import utils.FileManagerModule;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new FileManagerModule());
        Microservice microservice = injector.getInstance(Microservice.class);
        microservice.start();

        /* The JVM runs shutdown hooks only in case of normal terminations. So, when an external force
         * kills the JVM process abruptly, the JVM won't get a chance to execute shutdown hooks. */
        Thread shutdownHook = new Thread(() -> {

        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}
