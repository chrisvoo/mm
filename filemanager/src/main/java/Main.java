
public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        new Microservice()
                .loadEnvVars()
                .start();
    }
}
