package uk.ac.ed.inf;

import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import uk.ac.ed.inf.ilp.data.Order;
import java.util.List;

/**
 * Class responsible for converting order and flight path data into JSON format.
 */
public class FileOutputter {

    /**
     * Converts an array of Order objects into a JSON string.
     *
     * @param orders Array of Order objects.
     * @return A JSON string representing the array of orders.
     */
    public static String writeDeliveryJson(String date, Order[] orders) {
        JSONArray deliveryArray = new JSONArray();
        for (Order order : orders) {
            System.out.println("Order No before JSON conversion: " + order.getOrderNo());

            JSONObject orderObject = new JSONObject();
            orderObject.put("orderNo", order.getOrderNo());
            orderObject.put("orderStatus", order.getOrderStatus().toString());
            orderObject.put("orderValidationCode", order.getOrderValidationCode().toString());
            orderObject.put("costInPence", order.getPriceTotalInPence());
            deliveryArray.put(orderObject);
        }
        return deliveryArray.toString();
    }


    /**
     * Converts a list of Node objects into a JSON string for flight paths.
     *
     * @param Nodes A list of Node objects representing flight paths.
     * @return A JSON string representing the flight paths.
     */
    public static String writeFlightpathJson(List<Node> Nodes)  {
        JSONArray FlightPath = new JSONArray();
        for (Node node: Nodes) {
            JSONObject path = new JSONObject();
            // Extracting node details and adding them to the JSONObject
            path.put("orderNo", node.getOrderNo().orElse("")); // Unwrap Optional and provide a default value if it's empty
            path.put("fromLongitude", node.getStart().lng());
            path.put("fromLatitude", node.getStart().lat());
            path.put("angle", node.getAngle());
            path.put("toLongitude", node.getEnd().lng());
            path.put("toLatitude", node.getEnd().lat());
            // Adding the JSONObject to the JSONArray
            FlightPath.put(path);
        }
        return FlightPath.toString();
    }

    /**
     * Converts a list of Node objects into a GeoJSON string.
     *
     * @param Nodes A list of Node objects representing geographic locations.
     * @return A GeoJSON string representing the geographic locations.
     */
    public static String writeGeoJson(List<Node> Nodes) {
        JsonObject FeatureCollection = new JsonObject();
        FeatureCollection.addProperty("type", "FeatureCollection");
        JsonArray features = new JsonArray();
        JsonObject feature = new JsonObject();
        feature.addProperty("type", "Feature");
        feature.addProperty("properties", "NULL");

        JsonObject geometry = new JsonObject();
        geometry.addProperty("type", "LineString");
        JsonArray coords = new JsonArray();

        // Building the coordinates array from Node objects
        for (Node node : Nodes) {
            JsonArray lngLat = new JsonArray();
            lngLat.add(node.getStart().lng());
            lngLat.add(node.getStart().lat());
            coords.add(lngLat);
        }

        geometry.add("coordinates", coords);
        feature.add("geometry", geometry);
        features.add(feature);
        FeatureCollection.add("features", features);

        return FeatureCollection.toString();
    }
}
