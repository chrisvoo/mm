package src;

import utils.EnvVars;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class FileManagerClient {
    private HttpClient client;
    private EnvVars envVars;

    public FileManagerClient() {
        this.client = HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.envVars = new EnvVars();
        this.envVars.loadEnvVars();
    }

    /**
     * Buils the query string from a map of parameters, taking car of the url encoding.
     * @param parameters The parameters
     * @return an encoded query string.
     */
    private String buildQueryString(HashMap<String, String> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry: parameters.entrySet()) {
            if (!sb.isEmpty()) {
                sb.append("&");
            }
            sb.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            sb.append('=');
            sb.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }

        return sb.toString();
    }

    /**
     * Builds a request that doesn't need any body.
     * @param method The HTTP method.
     * @param route The route
     * @param parameters Query string parameters.
     * @return The request.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     */
    private HttpRequest noBodyRequest(String method, String route, HashMap<String, String> parameters) throws URISyntaxException {
        String endpoint = String.format("http://localhost:%d", this.envVars.getPort());
        URI finalUri = new URI(endpoint + route + this.buildQueryString(parameters));

        return HttpRequest.newBuilder()
                .uri(finalUri)
                .header("Accept", "application/json")
                .method(method, HttpRequest.BodyPublishers.noBody())
                .build();
    }

    /**
     * Builds a request with a body.
     * @param method The HTTP method.
     * @param route The route
     * @param body The body (as a JSON string)
     * @param parameters Query string parameters.
     * @return The request.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     */
    private HttpRequest jsonBodyRequest(String method, String route, String body, HashMap<String, String> parameters) throws URISyntaxException {
        String endpoint = String.format("http://localhost:%d", this.envVars.getPort());
        URI finalUri = new URI(endpoint + route + this.buildQueryString(parameters));

        HttpRequest.BodyPublisher pubs = body != null && !body.trim().isEmpty()
                                         ? HttpRequest.BodyPublishers.ofString(body)
                                         : HttpRequest.BodyPublishers.noBody();

        return HttpRequest.newBuilder()
                .uri(finalUri)
                .header("Accept", "application/json")
                .header("Content-Type", "text/plain;charset=UTF-8")
                .method(method, pubs)
                .build();
    }

    /**
     * A GET request
     * @param route The route
     * @return The request.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     */
    public HttpRequest get(String route) throws URISyntaxException {
        return this.get(route, null);
    }

    /**
     * Sends a GET request.
     * @param route The route
     * @return The response as string.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     * @throws IOException if an I/O error occurs when sending or receiving.
     * @throws InterruptedException if the operation is interrupted.
     */
    public HttpResponse<String> sendGet(String route) throws URISyntaxException, IOException, InterruptedException {
        return this.client.send(this.get(route, null), HttpResponse.BodyHandlers.ofString());
    }

    /**
     * A GET request with query string params.
     * @param route The route.
     * @param parameters The query string params.
     * @return The request.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     */
    public HttpRequest get(String route, HashMap<String, String> parameters) throws URISyntaxException {
        return this.noBodyRequest("GET", route, parameters);
    }

    /**
     * Sends a GET request with parameters.
     * @param route The route
     * @param parameters The query string parameters.
     * @return The response as string.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     * @throws IOException if an I/O error occurs when sending or receiving.
     * @throws InterruptedException if the operation is interrupted.
     */
    public HttpResponse<String> sendGet(String route, HashMap<String, String> parameters) throws URISyntaxException, IOException, InterruptedException {
        return this.client.send(this.get(route, parameters), HttpResponse.BodyHandlers.ofString());
    }


    /**
     * A DELETE request.
     * @param route The route.
     * @return The request.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     */
    public HttpRequest delete(String route) throws URISyntaxException {
        return this.noBodyRequest("DELETE", route, null);
    }

    /**
     * A DELETE request with parameters
     * @param route The route.
     * @return The request.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     */
    public HttpRequest delete(String route, HashMap<String, String> parameters) throws URISyntaxException {
        return this.noBodyRequest("DELETE", route, parameters);
    }

    /**
     * Sends a DELETE request with parameters.
     * @param route The route
     * @param parameters The query string parameters.
     * @return The response as string.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     * @throws IOException if an I/O error occurs when sending or receiving.
     * @throws InterruptedException if the operation is interrupted.
     */
    public HttpResponse<String> sendDelete(String route, HashMap<String, String> parameters) throws URISyntaxException, IOException, InterruptedException {
        return this.client.send(this.delete(route, parameters), HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Sends a DELETE request.
     * @param route The route
     * @return The response as string.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     * @throws IOException if an I/O error occurs when sending or receiving.
     * @throws InterruptedException if the operation is interrupted.
     */
    public HttpResponse<String> sendDelete(String route) throws URISyntaxException, IOException, InterruptedException {
        return this.client.send(this.delete(route, null), HttpResponse.BodyHandlers.ofString());
    }

    /**
     * A POST request.
     * @param route The route.
     * @param body The body (as JSON).
     * @param parameters The query string params.
     * @return The request.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     */
    public HttpRequest post(String route, String body, HashMap<String, String> parameters) throws URISyntaxException {
        return this.jsonBodyRequest("POST", route, body, parameters);
    }

    /**
     * Send a POST request with parameters.
     * @param route The route.
     * @param body the body as string.
     * @param parameters The parameters.
     * @return The response as string.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     * @throws IOException if an I/O error occurs when sending or receiving.
     * @throws InterruptedException if the operation is interrupted.
     */
    public HttpResponse<String> sendPost(String route, String body, HashMap<String, String> parameters) throws URISyntaxException, IOException, InterruptedException {
        return this.client.send(this.post(route, body, parameters), HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Send a POST request.
     * @param route The route.
     * @param body the body as string.
     * @return The response as string.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     * @throws IOException if an I/O error occurs when sending or receiving.
     * @throws InterruptedException if the operation is interrupted.
     */
    public HttpResponse<String> sendPost(String route, String body) throws URISyntaxException, IOException, InterruptedException {
        return this.client.send(this.post(route, body, null), HttpResponse.BodyHandlers.ofString());
    }

    /**
     * A PUT request.
     * @param route The route.
     * @param body The body (as JSON).
     * @param parameters The query string params.
     * @return The request.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     */
    public HttpRequest put(String route, String body, HashMap<String, String> parameters) throws URISyntaxException {
        return this.jsonBodyRequest("PUT", route, body, parameters);
    }

    /**
     * Send a PUT request with parameters.
     * @param route The route.
     * @param body the body as string.
     * @param parameters The query string parameters.
     * @return The response as string.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     * @throws IOException if an I/O error occurs when sending or receiving.
     * @throws InterruptedException if the operation is interrupted.
     */
    public HttpResponse<String> sendPut(String route, String body, HashMap<String, String> parameters) throws URISyntaxException, IOException, InterruptedException {
        return this.client.send(this.put(route, body, parameters), HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Send a PUT request.
     * @param route The route.
     * @param body the body as string.
     * @return The response as string.
     * @throws URISyntaxException if the uri is null or violates RFC 2396
     * @throws IOException if an I/O error occurs when sending or receiving.
     * @throws InterruptedException if the operation is interrupted.
     */
    public HttpResponse<String> sendPut(String route, String body) throws URISyntaxException, IOException, InterruptedException {
        return this.client.send(this.put(route, body, null), HttpResponse.BodyHandlers.ofString());
    }
}
