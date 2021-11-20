import routes.ErrorRoutes;
import utils.EnvVars;
import utils.JsonTransformer;

import static spark.Spark.*;

public class Microservice {
    private EnvVars envVars;

    public Microservice loadEnvVars() {
        this.envVars = new EnvVars();
        return this;
    }

    public void start() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            port(envVars.getPort());
            get("/hello", "application/json", (req, res) -> "Hello World", new JsonTransformer());


            // Error handler routes
            ErrorRoutes.routes();
        } catch (ClassNotFoundException e) {
            System.err.println("It seems you're lacking the MySQL Connector driver");
            stop();
        }
    }
}
