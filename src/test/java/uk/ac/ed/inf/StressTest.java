package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class StressTest {

    @Test
    void testStress() {
        String[] dates = { "2023-02-01", "2023-02-02", "2023-02-03", "2023-02-04", "2023-02-05",
                "2023-02-06", "2023-02-07", "2023-02-08", "2023-02-09", "2023-02-10",
                "2023-02-11", "2023-02-12", "2023-02-13", "2023-02-14", "2023-02-15",
                "2023-02-16", "2023-02-17", "2023-02-18", "2023-02-19", "2023-02-20",
                "2023-02-21", "2023-02-22", "2023-02-23", "2023-02-24", "2023-02-25",
                "2023-02-26", "2023-02-27", "2023-02-28"
        };

        String restServerUrl = "https://ilp-rest.azurewebsites.net/";
        long startTime = System.currentTimeMillis();

        performStressTest(dates, restServerUrl);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }

    private void performStressTest(String[] dates, String restServerUrl) {
        assertDoesNotThrow(() -> runSystemSimulation(dates, restServerUrl));
    }

    private void runSystemSimulation(String[] dates, String restServerUrl) throws IOException, InterruptedException {
        // Fetch orders for all dates
        List<Order> allOrders = retrieveOrders(dates, restServerUrl);
        performSystemOperations(allOrders, restServerUrl);
    }

    private List<Order> retrieveOrders(String[] dates, String restServerUrl) throws IOException, InterruptedException {
        List<Order> allOrders = new ArrayList<>();
        for (String date : dates) {
            allOrders.addAll(Arrays.asList(RestService.restOrder(restServerUrl, date)));
        }
        return allOrders;
    }

    private void performSystemOperations(List<Order> allOrders, String restServerUrl) throws IOException, InterruptedException {
        Restaurant[] restaurants = RestService.restResaurant(restServerUrl);
        NamedRegion centralArea = RestService.restCentralArea(restServerUrl);
        NamedRegion[] noFlyZones = RestService.restNoFlyZone(restServerUrl);

        validateOrders(allOrders, restaurants);
        List<Node> paths = calculateFlightPaths(allOrders, noFlyZones, centralArea, restaurants);
        writeResultFiles("2023-11-15", allOrders.toArray(new Order[0]), paths);
    }

    private void validateOrders(List<Order> allOrders, Restaurant[] restaurants) {
        System.out.println("orders...");
        OrderValidator orderValidator = new OrderValidator();
        allOrders.forEach(order -> orderValidator.validateOrder(order, restaurants));
    }

    private List<Node> calculateFlightPaths(List<Order> allOrders, NamedRegion[] noFlyZones, NamedRegion centralArea, Restaurant[] restaurants) {
        System.out.println("flightpaths...");
        App app = new App(noFlyZones, centralArea, restaurants, allOrders);
        return app.routeCalculator();
    }

    private void writeResultFiles(String date, Order[] orders, List<Node> paths) throws IOException {
        FileOutputter createFiles = new FileOutputter();
        System.out.println("deliveries...");
        createFiles.writeDeliveryJson(date, orders);
        System.out.println("flightpaths...");
        createFiles.writeFlightpathJson(paths);
        System.out.println("drones...");
        createFiles.writeGeoJson(paths);
    }}
