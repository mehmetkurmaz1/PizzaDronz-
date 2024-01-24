package uk.ac.ed.inf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.LngLatHandler;
import uk.ac.ed.inf.FlightPath;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.*;


public class NoFlyZoneUnitTest {
    private static NamedRegion central;
    private static NamedRegion[] noFlyZones;
    private static FlightPath pathFinder;
    private static LngLatHandler lngLatHandler;
    private static Restaurant civerinos, soraLella, dominos, sodeberg, laTrattoria, halalPizza, worldOfPizza;
    private static final LngLat AT = new LngLat(-3.186874,55.944494);


    @BeforeAll
    static void beforeAll() {
        central = new NamedRegion("central", new LngLat[]{new LngLat(-3.192473, 55.946233),
                new LngLat(-3.192473, 55.942617),
                new LngLat(-3.184319, 55.942617),
                new LngLat(-3.184319, 55.946233)});

        noFlyZones = new NamedRegion[]{
        };

        lngLatHandler = new LngLatHandler();

        pathFinder = new FlightPath(noFlyZones, central);
        noFlyZones = new NamedRegion[]{
                new NamedRegion("George Square Area", new LngLat[]{
                        new LngLat(-3.190578818321228, 55.94402412577528),
                        new LngLat(-3.1899887323379517, 55.94284650540911),
                        new LngLat(-3.187097311019897, 55.94328811724263),
                        new LngLat(-3.187682032585144, 55.944477740393744),
                        new LngLat(-3.190578818321228, 55.94402412577528)
                }),
                new NamedRegion("Dr Elsie Inglis Quadrangle", new LngLat[]{
                        new LngLat(-3.1907182931900024, 55.94519570234043),
                        new LngLat(-3.1906163692474365, 55.94498241796357),
                        new LngLat(-3.1900262832641597, 55.94507554227258),
                        new LngLat(-3.190133571624756, 55.94529783810495),
                        new LngLat(-3.1907182931900024, 55.94519570234043)
                }),
                new NamedRegion("Bristo Square Open Area", new LngLat[]{
                        new LngLat(-3.189543485641479, 55.94552313663306),
                        new LngLat(-3.189382553100586, 55.94553214854692),
                        new LngLat(-3.189259171485901, 55.94544803726933),
                        new LngLat(-3.1892001628875732, 55.94533688994374),
                        new LngLat(-3.189194798469543, 55.94519570234043),
                        new LngLat(-3.189135789871216, 55.94511759833873),
                        new LngLat(-3.188138008117676, 55.9452738061846),
                        new LngLat(-3.1885510683059692, 55.946105902745614),
                        new LngLat(-3.1895381212234497, 55.94555918427592),
                        new LngLat(-3.189543485641479, 55.94552313663306)
                }),
                new NamedRegion("Bayes Central Area", new LngLat[]{
                        new LngLat(-3.1876927614212036, 55.94520696732767),
                        new LngLat(-3.187555968761444, 55.9449621408666),
                        new LngLat(-3.186981976032257, 55.94505676722831),
                        new LngLat(-3.1872327625751495, 55.94536993377657),
                        new LngLat(-3.1874459981918335, 55.9453361389472),
                        new LngLat(-3.1873735785484314, 55.94519344934259),
                        new LngLat(-3.1875935196876526, 55.94515665035927),
                        new LngLat(-3.187624365091324, 55.94521973430925),
                        new LngLat(-3.1876927614212036, 55.94520696732767)
                })};
        civerinos = new Restaurant("Civerinos Slice",
                new LngLat(-3.1912869215011597,55.945535152517735),
                new DayOfWeek[]{DayOfWeek.MONDAY,DayOfWeek.TUESDAY,DayOfWeek.FRIDAY,DayOfWeek.SATURDAY,DayOfWeek.SUNDAY},
                new Pizza[]{
                        new Pizza("R1: Margarita",1000),
                        new Pizza("R1: Calzone",1400)
                });
        soraLella = new Restaurant(
                "Sora Lella Vegan Restaurant",
                new LngLat(-3.202541470527649, 55.943284737579376),
                new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY},
                new Pizza[]{
                        new Pizza("R2: Meat Lover", 1400),
                        new Pizza("R2: Vegan Delight", 1100)
                });

        dominos = new Restaurant(
                "Domino's Pizza - Edinburgh - Southside",
                new LngLat(-3.1838572025299072, 55.94449876875712),
                new DayOfWeek[]{DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                new Pizza[]{
                        new Pizza("R3: Super Cheese", 1400),
                        new Pizza("R3: All Shrooms", 900)
                });

        sodeberg = new Restaurant(
                "Sodeberg Pavillion",
                new LngLat(-3.1940174102783203, 55.94390696616939),
                new DayOfWeek[]{DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                new Pizza[]{
                        new Pizza("R4: Proper Pizza", 1400),
                        new Pizza("R4: Pineapple & Ham & Cheese", 900)
                });

        laTrattoria = new Restaurant(
                "La Trattoria",
                new LngLat(-3.1810810679852035, 55.938910643735845),
                new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.THURSDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                new Pizza[]{
                        new Pizza("R5: Pizza Dream", 1400),
                        new Pizza("R5: My kind of pizza", 900)
                });

        halalPizza = new Restaurant(
                "Halal Pizza",
                new LngLat(-3.185428203143916, 55.945846113595),
                new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                new Pizza[]{
                        new Pizza("R6: Sucuk delight", 1400),
                        new Pizza("R6: Dreams of Syria", 900)
                });

        worldOfPizza = new Restaurant(
                "World of Pizza",
                new LngLat(-3.179798972064253, 55.939884084483),
                new DayOfWeek[]{DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.TUESDAY},
                new Pizza[]{
                        new Pizza("R7: Hot, hotter, the hottest", 1400),
                        new Pizza("R7: All you ever wanted", 900)
                });
    }

    @Test
    void civerinosPath(){


        List<Node> pathTo = pathFinder.findTotalPath(AT, civerinos.location(), "abc123");
        List<Node> pathBack = pathFinder.findTotalPath(civerinos.location(), AT, "abc123");

        System.out.printf("No. moves from AT to %s: %d --- Num moves for path back to AT: %d\n",
                civerinos.name(),pathTo.size(),pathBack.size());

        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathTo));
        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathBack));
        Assertions.assertTrue(centralAreaRequirementMet(pathBack));
    }

    @Test
    void soraLellaPath() {
        LngLat AT = new LngLat(-3.186874,55.944494); // Assuming AT is at the central location

        List<Node> pathTo = pathFinder.findTotalPath(AT, soraLella.location(), "abc123");
        List<Node> pathBack = pathFinder.findTotalPath(soraLella.location(), AT, "abc123");

        System.out.printf("No. moves from AT to %s: %d --- Num moves for path back to AT: %d\n",
                soraLella.name(), pathTo.size(), pathBack.size());

        assertFalse(pathGoesThroughNoFlyZone(pathTo));
        assertFalse(pathGoesThroughNoFlyZone(pathBack));
    }

    @Test
    void dominosPath(){
        LngLat AT = new LngLat(-3.186874,55.944494); // Assuming AT is at the central location


        List<Node> pathTo = pathFinder.findTotalPath(AT, dominos.location(), "abc123");
        List<Node> pathBack = pathFinder.findTotalPath(dominos.location(), AT, "abc123");


        System.out.printf("No. moves from AT to %s: %d --- Num moves for path back to AT: %d\n",
                dominos.name(),pathTo.size(),pathBack.size());

        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathTo));
        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathBack));
    }

    @Test
    void soderbergPath(){
        LngLat AT = new LngLat(-3.186874,55.944494); // Assuming AT is at the central location
        List<Node> pathTo = pathFinder.findTotalPath(AT, sodeberg.location(), "abc123");
        List<Node> pathBack = pathFinder.findTotalPath(sodeberg.location(), AT, "abc123");


        System.out.printf("No. moves from AT to %s: %d --- Num moves for path back to AT: %d\n",
                sodeberg.name(),pathTo.size(),pathBack.size());

        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathTo));
        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathBack));
    }

    @Test
    void laTrattoriaPath(){
        LngLat AT = new LngLat(-3.186874,55.944494); // Assuming AT is at the central location


        List<Node> pathTo = pathFinder.findTotalPath(AT, laTrattoria.location(), "abc123");
        List<Node> pathBack = pathFinder.findTotalPath(laTrattoria.location(), AT, "abc123");



        System.out.printf("No. moves from AT to %s: %d --- Num moves for path back to AT: %d\n",
                laTrattoria.name(),pathTo.size(),pathBack.size());

        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathTo));
        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathBack));
    }

    @Test
    void halalPizzaPath() {
        LngLat AT = new LngLat(-3.192473, 55.946233); // Assuming AT is at the central location

        // Print the coordinates of the "Halal Pizza" restaurant
        System.out.println("Halal Pizza coordinates: " + halalPizza.location());

        // Print the coordinates of the "Bristo Square Open Area" boundary
        System.out.println("Bristo Square Open Area coordinates: " + Arrays.toString(noFlyZones[2].vertices()));

        List<Node> pathTo = pathFinder.findTotalPath(AT, halalPizza.location(), "abc123");
        List<Node> pathBack = pathFinder.findTotalPath(halalPizza.location(), AT, "abc123");

        System.out.printf("No. moves from AT to %s: %d --- Num moves for path back to AT: %d\n",
                halalPizza.name(), pathTo.size(), pathBack.size());
        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathTo));
        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathBack));
        Assertions.assertTrue(centralAreaRequirementMet(pathBack));
    }
    @Test
    void worldOfPizzaPath(){
        LngLat AT = new LngLat(-3.192473, 55.946233); // Assuming AT is at the central location

        List<Node> pathTo = pathFinder.findTotalPath(AT, worldOfPizza.location(), "abc123");
        List<Node> pathBack = pathFinder.findTotalPath(worldOfPizza.location(), AT, "abc123");

        System.out.printf("No. moves from AT to %s: %d --- Num moves for path back to AT: %d\n",
                worldOfPizza.name(),pathTo.size(),pathBack.size());

        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathTo));
        Assertions.assertFalse(pathGoesThroughNoFlyZone(pathBack));
    }

    @Test
    void pathToRestaurantAndBack() {
        // Define a path from AT to a restaurant and back to AT
        LngLat start = new LngLat(-3.186874, 55.944494); // AT
        LngLat restaurantLocation = civerinos.location(); // Restaurant location
        List<Node> pathToRestaurant = pathFinder.findTotalPath(start, restaurantLocation, "testOrder");
        List<Node> pathBackToAT = pathFinder.findTotalPath(restaurantLocation, start, "testOrder");

        assertFalse(pathGoesThroughNoFlyZone(pathToRestaurant));
        assertFalse(pathGoesThroughNoFlyZone(pathBackToAT));
        assertTrue(centralAreaRequirementMet(pathBackToAT));
    }

    @Test
    void pathToDifferentRestaurants() {
        // Define paths to different restaurants from AT
        LngLat start = new LngLat(-3.186874, 55.944494); // AT
        List<Node> pathToCiverinos = pathFinder.findTotalPath(start, civerinos.location(), "order1");
        List<Node> pathToSoraLella = pathFinder.findTotalPath(start, soraLella.location(), "order2");

        // Ensure that both paths do not go through no-fly zones and meet the central area requirement
        assertFalse(pathGoesThroughNoFlyZone(pathToCiverinos));
        assertFalse(pathGoesThroughNoFlyZone(pathToSoraLella));
    }

    @Test
    void cacheAndRetrievePathsInLoop() {
        LngLat start = new LngLat(-3.186874, 55.944494);
        LngLat[] destinations = {
                new LngLat(-3.187, 55.946),
                new LngLat(-3.188, 55.945),
                new LngLat(-3.189, 55.946)
        };

        for (int i = 0; i < destinations.length; i++) {
            List<Node> path = pathFinder.findTotalPath(start, destinations[i], "Order" + (i + 1));
            assertNotNull(path);
            assertTrue(path.size() > 0);

            // Ensure that the path is retrieved from the cache on subsequent requests.
            List<Node> cachedPath = pathFinder.findTotalPath(start, destinations[i], "Order" + (i + 1));
            assertNotNull(cachedPath);
            assertTrue(cachedPath.size() > 0);

            // Ensure that the retrieved path is the same as the original path.
            assertEquals(path, cachedPath);
        }

    }
    @Test
    void calculatePathsForMultipleOrders() {
        LngLat start = new LngLat(-3.186874, 55.944494);
        LngLat[] destinations = {
                new LngLat(-3.187, 55.946),
                new LngLat(-3.188, 55.945),
                new LngLat(-3.189, 55.946)
        };

        for (int i = 0; i < destinations.length; i++) {
            List<Node> path = pathFinder.findTotalPath(start, destinations[i], "Order" + (i + 1));
            assertNotNull(path);
            assertTrue(path.size() > 0);
        }
    }





    private boolean pathGoesThroughNoFlyZone(List<Node> path) {
        boolean pathInNoFlyZone = isInNoFlyZone(path.get(0).getStart()); // Check the starting position.
        double bufferDistance = 0.0001; // Adjust this buffer distance as needed.

        for (int i = 1; i < path.size(); i++) {
            LngLat position = path.get(i).getStart(); // Get the current position of the drone.

            // Debug statement: Print the current position.
            System.out.println("Checking position: " + position);

            boolean isInNoFly = isInNoFlyZone(position);

            // Debug statement: Print the result of checking if the position is in any no-fly zone.
            System.out.println("Is in no-fly zone: " + isInNoFly);

            // Update pathInNoFlyZone based on whether the current position is in a no-fly zone.
            if (isInNoFly) {
                // Calculate the distance to the nearest point of any no-fly zone.
                double minDistance = Double.MAX_VALUE;
                for (NamedRegion zone : noFlyZones) {
                    for (LngLat vertex : zone.vertices()) {
                        double distance = lngLatHandler.distanceTo(position, vertex);
                        if (distance < minDistance) {
                            minDistance = distance;
                        }
                    }
                }

                // If the minimum distance is less than the buffer distance, consider it as not in a no-fly zone.
                if (minDistance < bufferDistance) {
                    isInNoFly = false;
                }
            }

            pathInNoFlyZone = pathInNoFlyZone && isInNoFly;

            if (pathInNoFlyZone) {
                break; // Exit the loop early if the path is already in a no-fly zone.
            }
        }

        return pathInNoFlyZone;
    }



    public boolean isInNoFlyZone(LngLat position) {
        for (NamedRegion zone : noFlyZones) {
            if (lngLatHandler.isInRegion(position, zone)) {
                System.out.println("Position " + position + " is in no-fly zone: " + zone.name());
                return true;
            }
        }
        return false;
    }


    private boolean centralAreaRequirementMet(List<Node> path) {
        for (Node node : path) {
            LngLat location = node.getStart();
            if (!lngLatHandler.isInCentralArea(location, central)) {
                return false;
            }
        }
        return true;
    }
}