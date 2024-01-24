package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import static uk.ac.ed.inf.ilp.constant.SystemConstants.DRONE_IS_CLOSE_DISTANCE;
import static uk.ac.ed.inf.ilp.constant.SystemConstants.DRONE_MOVE_DISTANCE;

/**
 * The LngLatHandler class implements the LngLatHandling interface
 * and provides functionality for calculating distances and positions
 * based on longitude and latitude, as well as determining if a position is
 * within a specified region.
 */
public class LngLatHandler implements LngLatHandling {

    public LngLatHandler() {
    }

    /**
     * Calculates the Euclidean distance between two geographical positions.
     *
     * @param startPosition The starting position.
     * @param endPosition   The ending position.
     * @return The Euclidean distance between the two positions.
     */
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {
        double deltaX = startPosition.lng() - endPosition.lng();
        double deltaY = startPosition.lat() - endPosition.lat();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    /**
     * Checks if the startPosition is close to otherPosition, based on a predefined distance threshold.
     *
     * @param startPosition The starting position.
     * @param otherPosition The position to compare to.
     * @return true if startPosition is close to otherPosition; false otherwise.
     */
    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
        return distanceTo(startPosition, otherPosition) < DRONE_IS_CLOSE_DISTANCE;
    }

    /**
     * Determines whether a given position is inside a specified region.
     * This method uses the ray-casting algorithm to determine if the point is inside the polygon.
     *
     * @param position The position to check.
     * @param region   The region defined by its vertices.
     * @return true if the position is inside the region; false otherwise.
     */
    @Override
    public boolean isInRegion(LngLat position, NamedRegion region) {
        LngLat[] vertices = region.vertices();
        int n = vertices.length;
        if (n < 3) {
            return false;
        }

        // Ray-casting algorithm implementation
        LngLat extreme = new LngLat(9999.99, position.lat());
        int count = 0, i = 0;

        do {
            int next = (i + 1) % n;
            if (doIntersect(vertices[i], vertices[next], position, extreme)) {
                if (orientation(vertices[i], position, vertices[next]) == 0) {
                    return onSegment(vertices[i], position, vertices[next]);
                }
                count++;
            }
            i = next;
        } while (i != 0);

        // Return true if count is odd, false otherwise
        return (count % 2 == 1); // Same as (count%2 == 1)
    }

    /**
     * Determines whether a possible path between two points goes through a No-Fly Zone
     *
     * @param p1         LngLat of the start of the possible path
     * @param p2         LngLat of the end of the possible path
     * @param noFlyZones Array of NamedRegion objects representing No-Fly Zones
     * @return True if the path between p1 and p2 goes through a No-Fly Zone edge
     */
    public boolean pathGoesThroughNoFlyZones(LngLat p1, LngLat p2, NamedRegion[] noFlyZones) {
        LineSegment pathSegment = new LineSegment(p1, p2);
        for (NamedRegion noFlyZone : noFlyZones) {
            LngLat[] vertices = noFlyZone.vertices();
            int n = vertices.length;
            for (int i = 0; i < n; i++) {
                // Make edge of noFlyZone
                LineSegment noFlyZoneEdge = new LineSegment(vertices[i], vertices[(i + 1) % n]);
                if (doIntersect(pathSegment.start, pathSegment.end, noFlyZoneEdge.start, noFlyZoneEdge.end)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Private methods are used as utility functions within the class

    /**
     * Checks if the given point q lies on the line segment pr.
     *
     * @param p One end of the line segment.
     * @param q The point to check.
     * @param r The other end of the line segment.
     * @return true if q lies on the line segment pr; false otherwise.
     */
    private boolean onSegment(LngLat p, LngLat q, LngLat r) {
        // Check if q is within the bounds of the segment pr
        return q.lng() <= Math.max(p.lng(), r.lng()) && q.lng() >= Math.min(p.lng(), r.lng()) &&
                q.lat() <= Math.max(p.lat(), r.lat()) && q.lat() >= Math.min(p.lat(), r.lat());
    }

    /**
     * Determines the orientation of the triplet (p, q, r).
     *
     * @param p First point.
     * @param q Second point.
     * @param r Third point.
     * @return 0 if p, q, r are collinear; 1 if clockwise; 2 if counterclockwise.
     */
    private int orientation(LngLat p, LngLat q, LngLat r) {
        double val = (q.lat() - p.lat()) * (r.lng() - q.lng()) - (q.lng() - p.lng()) * (r.lat() - q.lat());
        if (val == 0) return 0; // Collinear
        return (val > 0) ? 1 : 2; // Clockwise or Counterclockwise
    }

    /**
     * Checks if line segments p1q1 and p2q2 intersect.
     *
     * @param p1 First point of the first line segment.
     * @param q1 Second point of the first line segment.
     * @param p2 First point of the second line segment.
     * @param q2 Second point of the second line segment.
     * @return true if the line segments intersect; false otherwise.
     */
    private boolean doIntersect(LngLat p1, LngLat q1, LngLat p2, LngLat q2) {
        // Orientation checks to determine intersection
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General and special cases check
        if (o1 != o2 && o3 != o4) return true;
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // No intersection
    }

    /**
     * Calculates the next position based on the current position and a movement angle.
     *
     * @param startPosition The current position.
     * @param angle         The angle of movement in degrees.
     * @return The new position after moving.
     */
    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {
        // Special case handling for angle value
        if (angle == 999) {
            return startPosition;
        } else if (0 > angle || angle > 360) {
            throw new IllegalArgumentException("Angle is not between  0 and 360");
        } else if (angle % 22.5 != 0) {
            throw new IllegalArgumentException("Angle is not multiple of 22.5");
        }

        // Calculate new longitude and latitude based on the angle
        double newLng = startPosition.lng() + DRONE_MOVE_DISTANCE * Math.cos(Math.toRadians(angle));
        double newLat = startPosition.lat() + DRONE_MOVE_DISTANCE * Math.sin(Math.toRadians(angle));
        return new LngLat(newLng, newLat);
    }

    /**
     * Represents testRay for isInRegion or edges of zones
     */
    private class LineSegment {
        private final LngLat start;
        private final LngLat end;

        private LineSegment(LngLat start, LngLat end) {
            this.start = start;
            this.end = end;
        }
    }
}
