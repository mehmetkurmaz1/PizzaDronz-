package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.data.Order;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * The RestService class provides static methods for interacting with a REST web service.
 * It includes functionality for sending HTTP requests and parsing responses.
 */
public class RestService {
    private static final HttpClient Client = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    /**
     * Sends an HTTP GET request to the specified URI.
     *
     * @param uri The URI to send the request to.
     * @return The response body as a string.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    private static String sendRequest(String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .timeout(Duration.ofMinutes(1)) // Set a timeout, e.g., 1 minute
                .build();
        HttpResponse<String> response = Client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Response code error: " + response.statusCode() + " for " + uri);
        }
        return response.body();
    }

    /**
     * Checks if the REST service is alive.
     *
     * @param url The base URL of the REST service.
     * @return The response body as a string.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static String isAlive(String url) throws IOException, InterruptedException {
        String urlString = url + "/isAlive";
        return sendRequest(urlString);
    }

    /**
     * Retrieves an array of Restaurant objects from the REST service.
     *
     * @param url The base URL of the REST service.
     * @return An array of Restaurant objects.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static Restaurant[] restResaurant(String url) throws IOException, InterruptedException {
        String urlString = url + "/restaurants";
        String response = sendRequest(urlString);
        return objectMapper.readValue(response, Restaurant[].class);
    }

    /**
     * Retrieves an array of Order objects for a specific date from the REST service.
     *
     * @param url The base URL of the REST service.
     * @param date The date for which orders are requested.
     * @return An array of Order objects.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static Order[] restOrder(String url, String date) throws IOException, InterruptedException {
        String urlString = url + "/orders/" + date;
        String response = sendRequest(urlString);
        return objectMapper.readValue(response, Order[].class);
    }

    /**
     * Retrieves the central area as a NamedRegion object from the REST service.
     *
     * @param url The base URL of the REST service.
     * @return A NamedRegion object representing the central area.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static NamedRegion restCentralArea(String url) throws IOException, InterruptedException {
        String urlString = url + "/centralArea";
        String response = sendRequest(urlString);
        return objectMapper.readValue(response, NamedRegion[].class)[0];
    }

    /**
     * Retrieves an array of no-fly zones as NamedRegion objects from the REST service.
     *
     * @param url The base URL of the REST service.
     * @return An array of NamedRegion objects representing no-fly zones.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the operation is interrupted.
     */
    public static NamedRegion[] restNoFlyZone(String url) throws IOException, InterruptedException {
        String urlString = url + "/noFlyZones";
        String response = sendRequest(urlString);
        return objectMapper.readValue(response, NamedRegion[].class);
    }
}
