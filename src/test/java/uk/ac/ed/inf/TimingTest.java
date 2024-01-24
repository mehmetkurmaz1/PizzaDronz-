package uk.ac.ed.inf;
import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static java.time.Duration.ofSeconds;


public class TimingTest {

    @Test
    void testMainPerformance() {
        String[] testDates = {"2023-11-15", "2023-11-16", "2023-11-17", "2023-11-18", "2023-11-19", "2023-11-20"};
        String testUrl = "https://ilp-rest.azurewebsites.net/";

        for (String date : testDates) {
            runPerformanceTestForDate(date, testUrl);
        }
    }

    private void runPerformanceTestForDate(String date, String url) {
        long startTime = System.currentTimeMillis();

        assertTimeoutPreemptively(ofSeconds(60), () -> runMain(date, url),
                "Execution for " + date + " exceeded 60 seconds.");

        long endTime = System.currentTimeMillis();
        System.out.println("Execution: " + date + " " + (endTime - startTime) + " milliseconds");
    }

    private void runMain(String date, String url) {
        try {
            NamedRegion[] noFlyZones = RestService.restNoFlyZone(url);
            NamedRegion centralArea = RestService.restCentralArea(url);
            Restaurant[] restaurants = RestService.restResaurant(url);
            Order[] orders = RestService.restOrder(url, date);

            List<Order> validOrders = filterValidOrders(orders, restaurants);

            App main = new App(noFlyZones, centralArea, restaurants, validOrders);
            String[] args = {date, url};
            main.main(args);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Order> filterValidOrders(Order[] orders, Restaurant[] restaurants) {
        OrderValidator validator = new OrderValidator();
        return Arrays.stream(orders)
                .filter(order -> validator.validateOrder(order, restaurants).getOrderStatus() != OrderStatus.INVALID)
                .collect(Collectors.toList());
    }
}