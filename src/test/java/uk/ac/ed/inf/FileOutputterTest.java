package uk.ac.ed.inf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;

import java.util.ArrayList;
import java.util.List;


public class FileOutputterTest extends TestCase {

    public FileOutputterTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(FileOutputterTest.class);
    }

    /**
     * Test for verifying GeoJSON output with a single feature.
     */
    public void testGeoJsonSingleFeature() {
        StringBuilder expectedBuilder = new StringBuilder();
        expectedBuilder.append("{\"type\":\"FeatureCollection\",\"features\":[");
        expectedBuilder.append("{\"type\":\"Feature\",\"properties\":\"NULL\",");
        expectedBuilder.append("\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[0.0,0.0]]}}");
        expectedBuilder.append("]}");
        String expected = expectedBuilder.toString();

        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new LngLat(0, 0), 0, new LngLat(10, 10), "Test"));

        String actual = FileOutputter.writeGeoJson(nodes);
        assertEquals(expected, actual);
    }

    /**
     * Test for verifying GeoJSON output with multiple features.
     */
    public void testGeoJsonFeaturesSequence() {
        String part1 = "{\"type\":\"FeatureCollection\",\"features\":[";
        String part2 = "{\"type\":\"Feature\",\"properties\":\"NULL\",";
        String part3 = "\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[0.0,0.0],[10.0,10.0]]}}";
        String expected = part1 + part2 + part3 + "]}";

        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new LngLat(0, 0), 0, new LngLat(10, 10), "Test"));
        nodes.add(new Node(new LngLat(10, 10), 0, new LngLat(20, 20), "Test"));

        String actual = FileOutputter.writeGeoJson(nodes);
        assertEquals(expected, actual);
    }

    /**
     * Test for verifying GeoJSON output when no features are present.
     */
    public void testGeoJsonNoFeatures() {
        String expected = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":\"NULL\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[]}}]}";

        List<Node> nodes = new ArrayList<>();

        String actual = FileOutputter.writeGeoJson(nodes);
        assertEquals(expected, actual);
    }

    /**
     * Test for verifying JSON output of delivery information with a single order.
     */
    public void testSingleDeliveryJsonOutput() {
        Order[] orders = new Order[1];
        Order order = new Order();
        order.setOrderNo("2E58F726");
        order.setOrderStatus(OrderStatus.DELIVERED);
        order.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        order.setPriceTotalInPence(100);
        orders[0] = order;

        String date = "2023-11-15"; // Valid date for testing
        StringBuilder expectedJson = new StringBuilder();
        expectedJson.append("[");
        expectedJson.append("{\"orderValidationCode\":\"NO_ERROR\",");
        expectedJson.append("\"orderNo\":\"2E58F726\",");
        expectedJson.append("\"orderStatus\":\"DELIVERED\",");
        expectedJson.append("\"costInPence\":100}");
        expectedJson.append("]");
        String expected = expectedJson.toString();

        String actual = FileOutputter.writeDeliveryJson(date, orders);
        assertEquals(expected, actual);
    }

    /**
     * Test for verifying JSON output of delivery information with multiple orders.
     */
    public void testMultipleDeliveriesJsonOutput() {
        Order[] orders = new Order[2];

        Order order1 = new Order();
        order1.setOrderNo("2E58F726");
        order1.setOrderStatus(OrderStatus.DELIVERED);
        order1.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        order1.setPriceTotalInPence(100);
        orders[0] = order1;

        Order order2 = new Order();
        order2.setOrderNo("2E58F727");
        order2.setOrderStatus(OrderStatus.DELIVERED);
        order2.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        order2.setPriceTotalInPence(100);
        orders[1] = order2;

        String date = "2023-11-15";
        String expected = String.format(
                "[{\"orderValidationCode\":\"NO_ERROR\",\"orderNo\":\"%s\",\"orderStatus\":\"DELIVERED\",\"costInPence\":100}," +
                        "{\"orderValidationCode\":\"NO_ERROR\",\"orderNo\":\"%s\",\"orderStatus\":\"DELIVERED\",\"costInPence\":100}]",
                order1.getOrderNo(), order2.getOrderNo());

        String actual = FileOutputter.writeDeliveryJson(date, orders);
        assertEquals(expected, actual);
    }

    /**
     * Test for verifying JSON output of delivery information when no orders are present.
     */
    public void testEmptyDeliveryJsonOutput() {
        Order[] orders = new Order[0];
        String date = "2023-11-15"; // Valid date for testing
        String expected = "[]";

        String actual = FileOutputter.writeDeliveryJson(date, orders);
        assertEquals(expected, actual);
    }


    /**
     * Test for verifying JSON output of flight path information with a single node.
     */
    public void testSingleFlightpathJsonOutput() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new LngLat(0, 0), 0, new LngLat(10, 10), "Test"));

        String expected = String.format(
                "[{\"orderNo\":\"%s\",\"fromLongitude\":%d,\"fromLatitude\":%d,\"angle\":%d,\"toLatitude\":%d,\"toLongitude\":%d}]",
                "Test", 0, 0, 0, 10, 10
        );

        String actual = FileOutputter.writeFlightpathJson(nodes);
        assertEquals(expected, actual);
    }

    /**
     * Test for verifying JSON output of flight path information with multiple nodes.
     */
    public void testMultipleFlightpathJsonOutput() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(new LngLat(0, 0), 0, new LngLat(10, 10), "Test1"));
        nodes.add(new Node(new LngLat(10, 10), 0, new LngLat(20, 20), "Test2"));

        String expected = String.format(
                "[{\"orderNo\":\"%s\",\"fromLongitude\":%d,\"fromLatitude\":%d,\"angle\":%d,\"toLatitude\":%d,\"toLongitude\":%d}," +
                        "{\"orderNo\":\"%s\",\"fromLongitude\":%d,\"fromLatitude\":%d,\"angle\":%d,\"toLatitude\":%d,\"toLongitude\":%d}]",
                "Test1", 0, 0, 0, 10, 10,
                "Test2", 10, 10, 0, 20, 20
        );

        String actual = FileOutputter.writeFlightpathJson(nodes);
        assertEquals(expected, actual);
    }

    /**
     * Test for verifying JSON output of flight path information when no nodes are present.
     */
    public void testEmptyFlightpathJsonOutput() {
        List<Node> nodes = new ArrayList<>();
        String expected = "[]";

        String actual = FileOutputter.writeFlightpathJson(nodes);
        assertEquals(expected, actual);
    }
}