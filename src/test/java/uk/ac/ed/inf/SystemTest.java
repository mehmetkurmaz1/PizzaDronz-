package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.ilp.data.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


class SystemTest {

    @Test
    void testSystem() {

        NamedRegion[] noFlyZones = TestConstraints.getNoFlyZones();
        NamedRegion centralArea = TestConstraints.getCentralArea();
        Restaurant[] definedRestaurants = TestConstraints.getDefinedRestaurants();
        String date = "2023-11-15";
        OrderValidator orderValidator = new OrderValidator();
        Order sampleOrder = createSampleOrder(date, definedRestaurants);

        Order validatedOrder = orderValidator.validateOrder(sampleOrder, definedRestaurants);

        App app = new App(noFlyZones, centralArea, definedRestaurants, Collections.singletonList(sampleOrder));

        List<Node> flightPathsFiles = app.routeCalculator();

        FileOutputter createFiles = new FileOutputter();
        createFiles.writeDeliveryJson(date, new Order[]{validatedOrder});
        createFiles.writeFlightpathJson(flightPathsFiles);
        createFiles.writeGeoJson(flightPathsFiles);

        readAndAssertJson("deliveries", date, -3.186874, 55.944494);
        readAndAssertJson("flightpath", date, -3.186874, 55.944494);
        assertFileCreation("drone", date);
    }

    private void readAndAssertJson(String fileType, String date, double expectedLongitude, double expectedLatitude) {
        Path filePath = Paths.get(System.getProperty("user.dir"), "resultFiles", fileType + "-" + date + ".json");
        Assert.assertTrue(fileType + " file should have been created", Files.exists(filePath));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(filePath.toFile());
            JsonNode firstEntry = jsonNode.get(0);

            if ("deliveries".equals(fileType)) {
                System.out.println("Order No in JSON: " + firstEntry.get("orderNo").asText());
            } else if ("flightpath".equals(fileType)) {
                assertLocation(firstEntry, "fromLongitude", "fromLatitude", expectedLongitude, expectedLatitude);
                int lastIndex = jsonNode.size() - 1;
                JsonNode finalMove = jsonNode.get(lastIndex);
                assertLocation(finalMove, "toLongitude", "toLatitude", expectedLongitude, expectedLatitude);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Exception occurred while reading or parsing the " + fileType + " file.");
        }
    }

    private void assertLocation(JsonNode entry, String lonKey, String latKey, double expectedLon, double expectedLat) {
        Assert.assertEquals(expectedLon, entry.get(lonKey).asDouble(), 0.0001);
        Assert.assertEquals(expectedLat, entry.get(latKey).asDouble(), 0.0001);
    }

    private void assertFileCreation(String fileType, String date) {
        Path filePath = Paths.get(System.getProperty("user.dir"), "resultFiles", fileType + "-" + date + ".geojson");
        Assert.assertTrue(fileType + " file should have been created", Files.exists(filePath));
    }


    private Order createSampleOrder(String date, Restaurant[] definedRestaurants) {
        Order sampleOrder = new Order();
        CreditCardInformation creditCardInfo = new CreditCardInformation("1234567891234567",
                "03/24",
                "123"
        );
        sampleOrder.setOrderNo("123456");
        System.out.println("Created Order No: " + sampleOrder.getOrderNo());

        sampleOrder.setCreditCardInformation(creditCardInfo);
        sampleOrder.setOrderDate((LocalDate.of(2023, 11, 15)));

        sampleOrder.setPriceTotalInPence(2400); //Total is pizzas + delivery charge of 100
        Pizza pizza1 = new Pizza("Super Cheese",1400);
        Pizza pizza2 = new Pizza("All Shrooms", 900);
        sampleOrder.setPizzasInOrder(new Pizza[]{pizza1, pizza2});

        return sampleOrder;
    }
}

