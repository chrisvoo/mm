import com.google.inject.Guice;
import com.google.inject.Injector;
import utils.FileManagerModule;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Injector injector = Guice.createInjector(new FileManagerModule());
        Microservice microservice = injector.getInstance(Microservice.class);
        microservice.start();
    }
}
