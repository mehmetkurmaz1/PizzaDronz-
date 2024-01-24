package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.data.*;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.io.IOException;

import static uk.ac.ed.inf.ilp.constant.OrderStatus.DELIVERED;


/**
 * Main application class for managing drone deliveries.
 */
public class App {

    private final NamedRegion[] noFlyZones;
    private final NamedRegion centralArea;
    private final Restaurant[] restaurants;
    private final List<Order> orders;
    private final LngLat dropOffLocation = new LngLat(-3.186874, 55.944494);

    /**
     * @param noFlyZones  Array of NamedRegion objects representing no-fly zones.
     * @param centralArea The central area of operation as a NamedRegion object.
     * @param restaurants Array of Restaurant objects available for picking up orders.
     * @param orders      List of orders to be delivered.
     */
    public App(NamedRegion[] noFlyZones, NamedRegion centralArea, Restaurant[] restaurants, List<Order> orders) {
        this.noFlyZones = noFlyZones;
        this.centralArea = centralArea;
        this.restaurants = restaurants;
        this.orders = orders;
    }

    /**
     * Calculates the routes for drone deliveries.
     *
     * @return List of Nodes representing the calculated paths.
     */
    public List<Node> routeCalculator() {
        FlightPath pathCalculator = new FlightPath(noFlyZones, centralArea);
        List<Node> paths = new ArrayList<>();

        for (Order order : orders) {
            try {
                LngLat restaurantLoc = getRestaurantLocation(order);
                List<Node> path = pathCalculator.findTotalPath(restaurantLoc, dropOffLocation, order.getOrderNo());
                paths.addAll(path);
                order.setOrderStatus(DELIVERED);
            } catch (RestaurantNotFoundException e) {
                System.err.println(e.getMessage());
            }
        }
        return paths;
    }

    /**
     * Retrieves the location of the restaurant for a given order.
     *
     * @param order The order for which the restaurant location is required.
     * @return LngLat object representing the location of the restaurant.
     * @throws RestaurantNotFoundException if no suitable restaurant is found for the order.
     */

    private LngLat getRestaurantLocation(Order order) throws RestaurantNotFoundException {
        Set<Pizza> pizzasInOrder = new HashSet<>(Arrays.asList(order.getPizzasInOrder()));
        for (Restaurant restaurant : restaurants) {
            Set<Pizza> menuSet = new HashSet<>(Arrays.asList(restaurant.menu()));
            if (menuSet.containsAll(pizzasInOrder)) {
                return restaurant.location();
            }
        }
        throw new RestaurantNotFoundException("Restaurant not found for order: " + order.getOrderNo());
    }

    // Exception class for situations where a restaurant cannot be found for an order.
    private static class RestaurantNotFoundException extends Exception {
        public RestaurantNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Validates the command-line arguments provided to the application.
     *
     * @param args The command-line arguments.
     * @throws IllegalArgumentException if the number of arguments is incorrect or if they don't match the expected format.
     */
    private static void validateArguments(String[] args) {
        // Ensure exactly two arguments are provided
        if (args.length != 2) {
            throw new IllegalArgumentException("Incorrect number of arguments. Expected 2 arguments: date and URL");
        }


        String date = args[0];
        String url = args[1];

        // Validate date format
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Date must be in the format YYYY-MM-DD");
        }

        // Validate URL format
        if (!url.matches("https://.*")) {
            throw new IllegalArgumentException("URL must start with https://");
        }
    }

    /**
     * Main method to run the application.
     *
     * @param args Command-line arguments: date and URL.
     */
    public static void main(String[] args) {
        try {
            validateArguments(args);
            String date = args[0];
            String url = args[1];

            // Check if the remote service is alive
            String serviceStatus = RestService.isAlive(url);
            if (!serviceStatus.equals("true")) {
                throw new IllegalStateException("Service is not alive");
            }

            // Retrieve data from the remote service
            Restaurant[] restaurants = RestService.restResaurant(url);
            Order[] orders = RestService.restOrder(url, date);
            NamedRegion centralArea = RestService.restCentralArea(url);
            NamedRegion[] noFlyZones = RestService.restNoFlyZone(url);

            // Validate orders
            OrderValidator validator = new OrderValidator();
            List<Order> validOrders = new ArrayList<>();
            for (Order order : orders) {
                if (validator.validateOrder(order, restaurants).getOrderStatus() != OrderStatus.INVALID) {
                    validOrders.add(order);
                }
            }

            // Calculate the flight paths for the drone
            App app = new App(noFlyZones, centralArea, restaurants, validOrders);
            List<Node> paths = app.routeCalculator();

            // Extract date components for file naming
            String year = date.substring(0, 4);
            String month = date.substring(5, 7);
            String day = date.substring(8, 10);

            // Create directory for result files
            new File("../resultfiles").mkdir();

            // Generate and write delivery information to a file
            String deliveriesFile = "./resultfiles/deliveries-" + year + "-" + month + "-" + day + ".json";
            writeFile(deliveriesFile, FileOutputter.writeDeliveryJson(date, orders));
            System.out.println("Delivery file generated: " + deliveriesFile);

            // Generate and write flight path information to a file
            String flightpathFile = "./resultfiles/flightpath-" + year + "-" + month + "-" + day + ".json";
            writeFile(flightpathFile, FileOutputter.writeFlightpathJson(paths));
            System.out.println("Flightpath file generated: " + flightpathFile);

            // Generate and write drone geo-location information to a file
            String droneFile = "./resultfiles/drone-" + year + "-" + month + "-" + day + ".geojson";
            writeFile(droneFile, FileOutputter.writeGeoJson(paths));
            System.out.println("Drone GeoJSON file generated: " + droneFile);

        } catch (IllegalArgumentException e) {
            // Handle the IllegalArgumentException here
            System.err.println("Invalid arguments: " + e.getMessage());
            // Optionally, print a usage message or exit the application gracefully
        } catch (Exception e) {
            // Print the stack trace in case of other exceptions
            e.printStackTrace();
        }
    }

    /**
     * Writes content to a file at the specified file path.
     *
     * @param filePath The file path where the content will be written.
     * @param content  The content to write to the file.
     * @throws IOException if an I/O error occurs.
     */
    private static void writeFile(String filePath, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(content);
        }
    }
}