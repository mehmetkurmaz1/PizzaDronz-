package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;

import java.util.*;

import static uk.ac.ed.inf.ilp.constant.OrderStatus.DELIVERED;

public class FlightPath {
    private final LngLat dropOffLocation = new LngLat(-3.186874, 55.944494);

    // Predefined angles for flight direction.
    private static final double[] ANGLES = {0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5, 180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5};

    // Central area where the drone can fly.
    private final NamedRegion centralArea;

    // No-fly zones to avoid during the flight.
    private final NamedRegion[] noFlyZones;

    // Cache to store previously calculated paths.
    private final Map<String, List<Node>> cachedPaths = new HashMap<>();

    // Handler for latitude and longitude calculations.
    private final LngLatHandler lngLatHandler = new LngLatHandler();

    /**
     * Constructor for FlightPath. Initializes no-fly zones and the central area.
     *
     * @param noFlyZones The array of no-fly zones.
     * @param centralArea The central area of operation.
     */
    public FlightPath(NamedRegion[] noFlyZones, NamedRegion centralArea) {
        this.noFlyZones = noFlyZones;
        this.centralArea = centralArea;
    }

    /**
     * Finds the total path for a flight from a restaurant to a drop-off location.
     *
     * @param restaurantLoc The starting location (restaurant).
     * @param dropOff The destination location (drop-off).
     * @param orderNo The order number associated with the flight.
     * @return List of Nodes representing the flight path.
     */
    public List<Node> findTotalPath(LngLat restaurantLoc, LngLat dropOff, String orderNo) {
        String key = generateCacheKey(restaurantLoc, dropOff);
        // Computes path if not already cached.
        return cachedPaths.computeIfAbsent(key, k -> generateAndCacheRoute(restaurantLoc, dropOff, orderNo));
    }

    /**
     * Generates and caches a new route.
     *
     * @param restaurantLoc The starting location (restaurant).
     * @param dropOff The destination location (drop-off).
     * @param orderNo The order number associated with the flight.
     * @return List of Nodes representing the combined route.
     */
    private List<Node> generateAndCacheRoute(LngLat restaurantLoc, LngLat dropOff, String orderNo) {
        List<Node> routeToRestaurant = findPath(dropOff, restaurantLoc, orderNo);
        List<Node> returnRoute = createReturnPath(routeToRestaurant, orderNo);
        List<Node> combinedRoute = new ArrayList<>(routeToRestaurant);
        combinedRoute.addAll(returnRoute);
        return combinedRoute;
    }

    /**
     * Finds the optimal path from a start point to an end point, avoiding no-fly zones.
     * The path is determined based on a set of predefined angles and the shortest distance
     * to the destination.
     *
     * @param start The starting location as a LngLat object.
     * @param end The destination location as a LngLat object.
     * @param orderNo The order number for which the path is being calculated.
     * @return List of Nodes representing the path from start to end.
     */
    private List<Node> findPath(LngLat start, LngLat end, String orderNo) {
        Set<LngLat> prevNodes = new HashSet<>();
        LngLat currentPosition = start;
        List<Node> route = new ArrayList<>();

        // Continues until the end point is close.
        while (!lngLatHandler.isCloseTo(currentPosition, end)) {
            double closestDistance = Double.MAX_VALUE;
            double chosenAngle = 0;

            // Chooses the best angle to move towards the end point.
            for (double angle : ANGLES) {
                LngLat nextPosition = lngLatHandler.nextPosition(currentPosition, angle);
                if (prevNodes.add(nextPosition) && isMovePermissible(currentPosition, nextPosition)) {
                    double distance = lngLatHandler.distanceTo(nextPosition, end);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        chosenAngle = angle;
                    }
                }
            }

            // Adds the node to the route and updates the current position.
            route.add(new Node(currentPosition, chosenAngle, lngLatHandler.nextPosition(currentPosition, chosenAngle), orderNo));
            currentPosition = lngLatHandler.nextPosition(currentPosition, chosenAngle);
        }
        // Adds the final node to the route.
        route.add(new Node(currentPosition, 999, currentPosition, orderNo));
        return route;
    }

    /**
     * Checks if a move from a current position to a next position is permissible.
     * A move is permissible if it doesn't enter a no-fly zone and adheres to the
     * central area constraints.
     *
     * @param current The current position as a LngLat object.
     * @param next The next position as a LngLat object.
     * @return boolean indicating whether the move is permissible or not.
     */
    private boolean isMovePermissible(LngLat current, LngLat next) {
        // Checks for no-fly zones and central area constraints.
        if (lngLatHandler.isInCentralArea(current, centralArea) || !lngLatHandler.isInCentralArea(next, centralArea)) {
            // Modify this line to use the LngLatHandler's method to check if the move is permissible.
            return !lngLatHandler.pathGoesThroughNoFlyZones(current, next, noFlyZones);
        }
        return true;
    }

    /**
     * Creates a return path from the given route. The return path is essentially
     * the reverse of the given route, with each direction adjusted by 180 degrees.
     *
     * @param route The initial route for which the return path is to be created.
     * @param orderNo The order number associated with the route.
     * @return List of Nodes representing the return path.
     */
    private List<Node> createReturnPath(List<Node> route, String orderNo) {
        List<Node> reversed = new ArrayList<>();
        for (Node node : route) {
            // Reverses each node's direction.
            if (node.getAngle() != 999) {
                Node reversedNode = new Node(node.getEnd(), (node.getAngle() + 180) % 360, node.getStart(), orderNo);
                reversed.add(reversedNode);
            }
        }
        Collections.reverse(reversed);
        // Adds the final hover node to the return route.
        LngLat endHover = reversed.get(reversed.size() - 1).getEnd();
        reversed.add(new Node(endHover, 999, endHover, orderNo));
        return reversed;
    }

    /**
     * Generates a unique cache key based on the start and end locations.
     * This key is used to cache and retrieve precomputed paths.
     *
     * @param loc1 The first location as a LngLat object.
     * @param loc2 The second location as a LngLat object.
     * @return String representing the unique cache key.
     */
    private String generateCacheKey(LngLat loc1, LngLat loc2) {
        return "KEY:" + loc1.lng() + loc1.lat() + loc2.lng() + loc2.lat();
    }


}

