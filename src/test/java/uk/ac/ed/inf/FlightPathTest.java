package uk.ac.ed.inf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class FlightPathTest extends TestCase {
    public FlightPathTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(FlightPathTest.class);
    }

    String SERVICE_URL = "https://ilp-rest.azurewebsites.net";

    public void testMockData() throws IOException, InterruptedException {
        try (MockedStatic<RestService> mockedService = Mockito.mockStatic(RestService.class)) {
            mockRestServiceMethods(mockedService);

            NamedRegion[] noFlyZones = RestService.restNoFlyZone(SERVICE_URL);
            NamedRegion centralArea = RestService.restCentralArea(SERVICE_URL);
            Restaurant[] nearbyRestaurants = RestService.restResaurant(SERVICE_URL);
            Order[] recentOrders = RestService.restOrder(SERVICE_URL, "2020-12-07");

            LngLat startPoint = new LngLat(-3.186874, 55.944494);
            LngLat endPoint = nearbyRestaurants[0].location();

            FlightPath droneFlightPath = new FlightPath(noFlyZones, centralArea);
            List<Node> flightPath = droneFlightPath.findTotalPath(endPoint, startPoint, recentOrders[0].getOrderNo());

            checkFlightPath(flightPath, noFlyZones);
        }
    }

    private void mockRestServiceMethods(MockedStatic<RestService> mockedService) {
        mockedService.when(() -> RestService.restNoFlyZone(SERVICE_URL)).thenReturn(new NamedRegion[]{
                new NamedRegion("NoFlyZone1", new LngLat[]{
                        new LngLat(0.2, 0.3),
                        new LngLat(0.3, 0.2),
                        new LngLat(0.2, 0.2),
                        new LngLat(0.3, 0.3)
                })
        });

        mockedService.when(() -> RestService.restCentralArea(SERVICE_URL)).thenReturn(
                new NamedRegion("central", new LngLat[]{
                        new LngLat(-3.192473, -55.946233),
                        new LngLat(-3.192473, 55.942617),
                        new LngLat(-3.184319, 55.942617),
                        new LngLat(-3.184319, 55.946233)
                })
        );

        mockedService.when(() -> RestService.restResaurant(SERVICE_URL)).thenReturn(new Restaurant[]{
                new Restaurant("myRestaurant",
                        new LngLat(-3.1912869215011597, 55.945535152517735),
                        new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.FRIDAY},
                        new Pizza[]{new Pizza("Pizza A", 1000),
                                new Pizza("Pizza B", 1000),
                                new Pizza("Pizza C", 1000),
                                new Pizza("Pizza D", 1000),
                        }
                )
        });

        mockedService.when(() -> RestService.restOrder(SERVICE_URL, "2020-12-07")).thenReturn(new Order[]{
                new Order("Test",
                        LocalDate.of(2023, 9, 1),
                        OrderStatus.DELIVERED, OrderValidationCode.NO_ERROR,
                        1100,
                        new Pizza[]{new Pizza("Pizza A", 1000)},
                        new CreditCardInformation("1234567891234567", "123", "12/21")
                )
        });
    }


    private void checkFlightPath(List<Node> path, NamedRegion[] restrictedZones) {
        assertNotNull(path);
        assertFalse(path.isEmpty());

        LngLatHandler zoneChecker = new LngLatHandler();
        int hoverCount = calculateHoverCount(path, restrictedZones);

        assertEquals(2, hoverCount);
    }

    private int calculateHoverCount(List<Node> path, NamedRegion[] restrictedZones) {
        int hoverCount = 0;
        for (Node point : path) {
            if (point.getAngle() == 999) {
                hoverCount++;
            }
            checkNodeAgainstNoFlyZones(point, restrictedZones);
        }
        return hoverCount;
    }

    private void checkNodeAgainstNoFlyZones(Node point, NamedRegion[] restrictedZones) {
        LngLatHandler zoneChecker = new LngLatHandler();
        for (NamedRegion zone : restrictedZones) {
            assertFalse(zoneChecker.isInRegion(point.getEnd(), zone));
        }
    }



    public void testMockData2() throws IOException, InterruptedException {
        try (MockedStatic<RestService> mockedRestService = Mockito.mockStatic(RestService.class)) {
            setupMockRestService(mockedRestService);

            NamedRegion[] restrictedAreas = RestService.restNoFlyZone(SERVICE_URL);
            NamedRegion centralZone = RestService.restCentralArea(SERVICE_URL);
            Restaurant[] diningSpots = RestService.restResaurant(SERVICE_URL);
            Order[] customerOrders = RestService.restOrder(SERVICE_URL, "2023-09-01");

            LngLat departureLocation = new LngLat(-3.186874, 55.944494);
            LngLat targetLocation = diningSpots[0].location();

            FlightPath droneRoute = new FlightPath(restrictedAreas, centralZone);
            List<Node> routePlan = droneRoute.findTotalPath(targetLocation, departureLocation, customerOrders[0].getOrderNo());

            assertFlightPath(routePlan, restrictedAreas);
        }
    }

    private void setupMockRestService(MockedStatic<RestService> mockedService) {
        mockedService.when(() -> RestService.restNoFlyZone(SERVICE_URL)).thenReturn(new NamedRegion[]{
                new NamedRegion("NoFlyZone1", new LngLat[]{
                        new LngLat(0.2, 0.3),
                        new LngLat(0.3, 0.2),
                        new LngLat(0.2, 0.2),
                        new LngLat(0.3, 0.3)
                })
        });

        mockedService.when(() -> RestService.restCentralArea(SERVICE_URL)).thenReturn(
                new NamedRegion("central", new LngLat[]{
                        new LngLat(-3.192473, -55.946233),
                        new LngLat(-3.192473, 55.942617),
                        new LngLat(-3.184319, 55.942617),
                        new LngLat(-3.184319, 55.946233)
                })
        );

        mockedService.when(() -> RestService.restResaurant(SERVICE_URL)).thenReturn(new Restaurant[]{
                new Restaurant("myRestaurant",
                        new LngLat(-3.1912869215011597, 55.945535152517735),
                        new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.FRIDAY},
                        new Pizza[]{new Pizza("Pizza A", 1000),
                                new Pizza("Pizza B", 1000),
                                new Pizza("Pizza C", 1000),
                                new Pizza("Pizza D", 1000),
                        }
                )
        });

        mockedService.when(() -> RestService.restOrder(SERVICE_URL, "2023-09-01")).thenReturn(new Order[]{
                new Order("Test",
                        LocalDate.of(2023, 9, 1),
                        OrderStatus.DELIVERED, OrderValidationCode.NO_ERROR,
                        1100,
                        new Pizza[]{new Pizza("Pizza A", 1000)},
                        new CreditCardInformation("1234567891234567", "123", "12/21")
                )
        });
    }

    private void assertFlightPath(List<Node> path, NamedRegion[] noFlyZones) {
        assertNotNull(path);
        assertFalse(path.isEmpty());

        LngLatHandler regionEvaluator = new LngLatHandler();
        int hoverInstances = 0;

        for (Node point : path) {
            hoverInstances += point.getAngle() == 999 ? 1 : 0;
            Arrays.stream(noFlyZones).forEach(zone -> assertFalse(regionEvaluator.isInRegion(point.getEnd(), zone)));
        }

        assertEquals(2, hoverInstances);
    }



    public void testPath() {
        // Setup test data
        LngLat start = new LngLat(0, 0);
        LngLat end = new LngLat(0.00015, 0.00015);
        NamedRegion[] noFlyZones = setupNoFlyZones();
        NamedRegion central = setupCentralRegion();

        // Perform test action
        FlightPath flightPath = new FlightPath(noFlyZones, central);
        List<Node> calculatedPath = flightPath.findTotalPath(end, start, "Test");

        // Assertions
        assertPathIsValid(calculatedPath);
        assertPathMatchesExpected(calculatedPath, setupExpectedPath());
    }

    private NamedRegion[] setupNoFlyZones() {
        return new NamedRegion[]{
                new NamedRegion("NoFlyZone1", new LngLat[]{
                        new LngLat(0.2, 0.3),
                        new LngLat(0.3, 0.2),
                        new LngLat(0.2, 0.2),
                        new LngLat(0.3, 0.3)
                }),
                new NamedRegion("NoFlyZone2", new LngLat[]{
                        new LngLat(0.5, 0.5),
                        new LngLat(0.6, 0.5),
                        new LngLat(0.5, 0.6),
                        new LngLat(0.6, 0.6)
                })
        };
    }

    private NamedRegion setupCentralRegion() {
        return new NamedRegion("central", new LngLat[]{
                new LngLat(-1, -1),
                new LngLat(-1, 1),
                new LngLat(1, -1),
                new LngLat(1, 1)
        });
    }

    private void assertPathIsValid(List<Node> path) {
        assertNotNull("Path should not be null", path);
        assertFalse("Path should not be empty", path.isEmpty());
    }

    private List<Node> setupExpectedPath() {
        List<Node> expectedPath = new ArrayList<>();
        expectedPath.add(new Node(new LngLat(0, 0), 45, new LngLat(1.0606601717798212E-4, 1.0606601717798211E-4), "Test"));
        expectedPath.add(new Node(new LngLat(1.0606601717798212E-4, 1.0606601717798211E-4), 999, new LngLat(1.0606601717798212E-4, 1.0606601717798211E-4), "Test"));
        expectedPath.add(new Node(new LngLat(1.0606601717798212E-4, 1.0606601717798211E-4), 225, new LngLat(0.0, 0.0), "Test"));
        expectedPath.add(new Node(new LngLat(0.0, 0.0), 999, new LngLat(0.0, 0.0), "Test"));
        return expectedPath;
    }

    private void assertPathMatchesExpected(List<Node> actualPath, List<Node> expectedPath) {
        for (int i = 0; i < actualPath.size(); i++) {
            Node actualNode = actualPath.get(i);
            Node expectedNode = expectedPath.get(i);

            assertEquals("Order number should be Test", actualNode.getOrderNo(), Optional.of("Test"));
            assertEquals("Angle should be match", actualNode.getAngle(), expectedNode.getAngle());
            assertEquals("Start should be match", actualNode.getStart(), expectedNode.getStart());
            assertEquals("End should be match", actualNode.getEnd(), expectedNode.getEnd());
        }
    }



    public void testRealData() throws IOException, InterruptedException {
        NamedRegion[] noFlyZones = RestService.restNoFlyZone(SERVICE_URL);
        NamedRegion centralArea = RestService.restCentralArea(SERVICE_URL);
        Restaurant[] restaurantList = RestService.restResaurant(SERVICE_URL);
        Order[] orderList = RestService.restOrder(SERVICE_URL, "2023-09-01");

        LngLat startPoint = new LngLat(-3.186874, 55.944494);
        LngLat endPoint = restaurantList[0].location();

        FlightPath pathCalculator = new FlightPath(noFlyZones, centralArea);
        List<Node> flightPath = pathCalculator.findTotalPath(endPoint, startPoint, orderList[0].getOrderNo());

        assertNotNull(flightPath);
        assertFalse(flightPath.isEmpty());

        LngLatHandler regionHandler = new LngLatHandler();

        int hoverCount = 0;
        for (Node current : flightPath) {
            if (current.getAngle() == 999) {
                hoverCount++;
            }
            for (NamedRegion zone : noFlyZones) {
                assertFalse(regionHandler.isInRegion(current.getEnd(), zone));
            }
        }

        assertEquals(2, hoverCount);
    }

    public void testRealData2() throws IOException, InterruptedException {
        NamedRegion[] exclusionZones = RestService.restNoFlyZone(SERVICE_URL);
        NamedRegion centralHub = RestService.restCentralArea(SERVICE_URL);
        Restaurant[] localRestaurants = RestService.restResaurant(SERVICE_URL);
        Order[] holidayOrders = RestService.restOrder(SERVICE_URL, "2023-12-25");

        LngLat departurePoint = new LngLat(-3.186874, 55.944494);
        LngLat destinationPoint = localRestaurants[0].location();

        FlightPath droneFlightPath = new FlightPath(exclusionZones, centralHub);
        List<Node> flightRoute = droneFlightPath.findTotalPath(destinationPoint, departurePoint, holidayOrders[0].getOrderNo());

        assertNotNull(flightRoute);
        assertFalse(flightRoute.isEmpty());

        LngLatHandler regionChecker = new LngLatHandler();

        int hoverEvents = 0;
        for (Node waypoint : flightRoute) {
            if (waypoint.getAngle() == 999) {
                hoverEvents++;
            }
            for (NamedRegion restrictedArea : exclusionZones) {
                assertFalse(regionChecker.isInRegion(waypoint.getEnd(), restrictedArea));
            }
        }

        assertEquals(2, hoverEvents);
    }
}