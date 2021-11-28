import com.google.inject.Guice;
import com.google.inject.Injector;
import java.util.logging.Logger;
import utils.FileManagerModule;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new FileManagerModule());
        Microservice microservice = injector.getInstance(Microservice.class);
        microservice.start();
    }
}
