package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;

import java.util.Optional;

/**
 * Helper class to store the start, end coordinates of a move, the angle of the move, and the order number.
 */
public final class Node {
    private final LngLat start;
    private final LngLat end;
    private double angle;
    private Optional<String> orderNo;

    /**
     * Constructs a Move with start and end coordinates, angle, and order number.
     *
     * @param start   the starting coordinates
     * @param angle   the angle of the move
     * @param end     the ending coordinates
     * @param orderNo the order number (can be null)
     */
    public Node(LngLat start, double angle, LngLat end, String orderNo) {
        this.start = start;
        this.angle = angle;
        this.end = end;
        this.orderNo = Optional.ofNullable(orderNo);
    }

    /**
     * Constructs a Move with start and end coordinates, and angle.
     *
     * @param start the starting coordinates
     * @param angle the angle of the move
     * @param end   the ending coordinates
     */
    public Node(LngLat start, double angle, LngLat end) {
        this(start, angle, end, null);
    }

// -----------------------------------GETTERS-SETTERS--------------------------------------------//

    public LngLat getStart() {
        return start;
    }

    public double getAngle() {
        return angle;
    }

    public LngLat getEnd() {
        return end;
    }

    public Optional<String> getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = Optional.ofNullable(orderNo);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "Node{" +
                "start=" + start +
                ", end=" + end +
                ", angle=" + angle +
                ", orderNo=" + orderNo +
                '}';
    }
}
