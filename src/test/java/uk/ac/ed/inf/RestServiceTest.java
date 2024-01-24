package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mockito.MockedStatic;
import org.mockito.Mockito;
import uk.ac.ed.inf.ilp.data.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


public class RestServiceTest extends TestCase {
    public RestServiceTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(RestServiceTest.class);
    }

    String SERVICE_URL = "https://ilp-rest.azurewebsites.net";
    private static final ObjectMapper objectMapper = new ObjectMapper();



    public void testRestaurant() throws IOException, InterruptedException {
        Restaurant[] restaurants = RestService.restResaurant(SERVICE_URL);
        String jsonData = "[{\"name\":\"Civerinos Slice\",\"location\":{\"lng\":-3.1912869215011597,\"lat\":55.945535152517735},\"openingDays\":[\"MONDAY\",\"TUESDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"],\"menu\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}]},{\"name\":\"Sora Lella Vegan Restaurant\",\"location\":{\"lng\":-3.202541470527649,\"lat\":55.943284737579376},\"openingDays\":[\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\"],\"menu\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}]},{\"name\":\"Domino's Pizza - Edinburgh - Southside\",\"location\":{\"lng\":-3.1838572025299072,\"lat\":55.94449876875712},\"openingDays\":[\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"],\"menu\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}]},{\"name\":\"Sodeberg Pavillion\",\"location\":{\"lng\":-3.1940174102783203,\"lat\":55.94390696616939},\"openingDays\":[\"TUESDAY\",\"WEDNESDAY\",\"SATURDAY\",\"SUNDAY\"],\"menu\":[{\"name\":\"R4: Proper Pizza\",\"priceInPence\":1400},{\"name\":\"R4: Pineapple & Ham & Cheese\",\"priceInPence\":900}]},{\"name\":\"La Trattoria\",\"location\":{\"lng\":-3.1810810679852035,\"lat\":55.938910643735845},\"openingDays\":[\"MONDAY\",\"THURSDAY\",\"SATURDAY\",\"SUNDAY\"],\"menu\":[{\"name\":\"R5: Pizza Dream\",\"priceInPence\":1400},{\"name\":\"R5: My kind of pizza\",\"priceInPence\":900}]},{\"name\":\"Halal Pizza\",\"location\":{\"lng\":-3.185428203143916,\"lat\":55.945846113595},\"openingDays\":[\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"SATURDAY\",\"SUNDAY\"],\"menu\":[{\"name\":\"R6: Sucuk delight\",\"priceInPence\":1400},{\"name\":\"R6: Dreams of Syria\",\"priceInPence\":900}]},{\"name\":\"World of Pizza\",\"location\":{\"lng\":-3.179798972064253,\"lat\":55.939884084483},\"openingDays\":[\"THURSDAY\",\"FRIDAY\",\"TUESDAY\"],\"menu\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}]}]";
        Restaurant[] expected = objectMapper.readValue(jsonData, Restaurant[].class);

        assertEquals(restaurants.length, expected.length);
        for (int i = 0; i < restaurants.length; i++) {
            Restaurant actual = restaurants[i];
            Restaurant exp = expected[i];

            assertEquals(actual.name(), exp.name());
            assertEquals(actual.location(), exp.location());
            assertOpeningDaysAndMenuMatch(actual, exp);
        }
    }

    private void assertOpeningDaysAndMenuMatch(Restaurant actual, Restaurant expected) {
        assertEquals(actual.openingDays().length, expected.openingDays().length);
        for (int j = 0; j < actual.openingDays().length; j++) {
            assertEquals(actual.openingDays()[j], expected.openingDays()[j]);
        }

        assertEquals(actual.menu().length, expected.menu().length);
        for (int j = 0; j < actual.menu().length; j++) {
            assertEquals(actual.menu()[j].name(), expected.menu()[j].name());
            assertEquals(actual.menu()[j].priceInPence(), expected.menu()[j].priceInPence());
        }
    }


    public void testNoFlyZone() throws IOException, InterruptedException {
        NamedRegion[] noFlyZones = fetchNoFlyZones();
        NamedRegion[] expected = createExpectedNoFlyZones();

        assertNoFlyZonesMatchExpected(noFlyZones, expected);
    }

    private NamedRegion[] fetchNoFlyZones() throws IOException, InterruptedException {
        return RestService.restNoFlyZone(SERVICE_URL);
    }

    private NamedRegion[] createExpectedNoFlyZones() throws IOException {
        String jsonData = "[{\"name\":\"George Square Area\",\"vertices\":[{\"lng\":-3.190578818321228,\"lat\":55.94402412577528},{\"lng\":-3.1899887323379517,\"lat\":55.94284650540911},{\"lng\":-3.187097311019897,\"lat\":55.94328811724263},{\"lng\":-3.187682032585144,\"lat\":55.944477740393744},{\"lng\":-3.190578818321228,\"lat\":55.94402412577528}]},{\"name\":\"Dr Elsie Inglis Quadrangle\",\"vertices\":[{\"lng\":-3.1907182931900024,\"lat\":55.94519570234043},{\"lng\":-3.1906163692474365,\"lat\":55.94498241796357},{\"lng\":-3.1900262832641597,\"lat\":55.94507554227258},{\"lng\":-3.190133571624756,\"lat\":55.94529783810495},{\"lng\":-3.1907182931900024,\"lat\":55.94519570234043}]},{\"name\":\"Bristo Square Open Area\",\"vertices\":[{\"lng\":-3.189543485641479,\"lat\":55.94552313663306},{\"lng\":-3.189382553100586,\"lat\":55.94553214854692},{\"lng\":-3.189259171485901,\"lat\":55.94544803726933},{\"lng\":-3.1892001628875732,\"lat\":55.94533688994374},{\"lng\":-3.189194798469543,\"lat\":55.94519570234043},{\"lng\":-3.189135789871216,\"lat\":55.94511759833873},{\"lng\":-3.188138008117676,\"lat\":55.9452738061846},{\"lng\":-3.1885510683059692,\"lat\":55.946105902745614},{\"lng\":-3.1895381212234497,\"lat\":55.94555918427592},{\"lng\":-3.189543485641479,\"lat\":55.94552313663306}]},{\"name\":\"Bayes Central Area\",\"vertices\":[{\"lng\":-3.1876927614212036,\"lat\":55.94520696732767},{\"lng\":-3.187555968761444,\"lat\":55.9449621408666},{\"lng\":-3.186981976032257,\"lat\":55.94505676722831},{\"lng\":-3.1872327625751495,\"lat\":55.94536993377657},{\"lng\":-3.1874459981918335,\"lat\":55.9453361389472},{\"lng\":-3.1873735785484314,\"lat\":55.94519344934259},{\"lng\":-3.1875935196876526,\"lat\":55.94515665035927},{\"lng\":-3.187624365091324,\"lat\":55.94521973430925},{\"lng\":-3.1876927614212036,\"lat\":55.94520696732767}]}]";
        return objectMapper.readValue(jsonData, NamedRegion[].class);
    }

    private void assertNoFlyZonesMatchExpected(NamedRegion[] actualNoFlyZones, NamedRegion[] expectedNoFlyZones) {
        assertEquals(actualNoFlyZones.length, expectedNoFlyZones.length);

        for (int i = 0; i < actualNoFlyZones.length; i++) {
            assertNamedRegionEquals(actualNoFlyZones[i], expectedNoFlyZones[i]);
        }
    }

    private void assertNamedRegionEquals(NamedRegion actualRegion, NamedRegion expectedRegion) {
        assertEquals(actualRegion.name(), expectedRegion.name());
        assertVerticesMatch(actualRegion.vertices(), expectedRegion.vertices());
    }

    private void assertVerticesMatch(LngLat[] actualVertices, LngLat[] expectedVertices) {
        assertEquals(actualVertices.length, expectedVertices.length);

        for (int j = 0; j < actualVertices.length; j++) {
            assertEquals(actualVertices[j], expectedVertices[j]);
        }
    }




    public void testCentralAreaFetcher() throws IOException, InterruptedException {
        NamedRegion centralArea = fetchCentralAreaFromRestService();
        NamedRegion expected = createExpectedNamedRegion();

        assertNamedRegionsEqual(centralArea, expected);
    }

    private NamedRegion fetchCentralAreaFromRestService() throws IOException, InterruptedException {
        return RestService.restCentralArea(SERVICE_URL);
    }

    private NamedRegion createExpectedNamedRegion() throws IOException {
        String jsonData = "{\"name\":\"central\",\"vertices\":[{\"lng\":-3.192473,\"lat\":55.946233},{\"lng\":-3.192473,\"lat\":55.942617},{\"lng\":-3.184319,\"lat\":55.942617},{\"lng\":-3.184319,\"lat\":55.946233}]}";
        return objectMapper.readValue(jsonData, NamedRegion.class);
    }

    private void assertNamedRegionsEqual(NamedRegion actual, NamedRegion expected) {
        assertEquals(actual.name(), expected.name());
        assertArrayEquals(actual.vertices(), expected.vertices());
    }


    public void testOrderFetcher() throws IOException, InterruptedException {
        Order[] orders = fetchOrdersForDate("2023-09-01");
        Order[] expected = createExpectedOrders();

        assertOrdersMatchExpected(orders, expected);
    }

    private Order[] fetchOrdersForDate(String date) throws IOException, InterruptedException {
        return RestService.restOrder(SERVICE_URL, date);
    }

    private Order[] createExpectedOrders() throws IOException {
        String jsonData = "[{\"orderNo\":\"45C59C9C\",\"orderDate\":\"2023-09-01\",\"orderStatus\":" +  "\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"7562\",\"creditCardExpiry\":\"09/24\",\"cvv\":\"159\"}},{\"orderNo\":\"0086F124\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4299725940827052\",\"creditCardExpiry\":\"07/09\",\"cvv\":\"582\"}},{\"orderNo\":\"10932BD3\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5205799423087864\",\"creditCardExpiry\":\"10/28\",\"cvv\":\"9937\"}},{\"orderNo\":\"47CA59E1\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":3140,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4973959484831922\",\"creditCardExpiry\":\"04/28\",\"cvv\":\"956\"}},{\"orderNo\":\"3E17FA09\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":498705280,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900},{\"name\":\"Pizza-Surprise \",\"priceInPence\":498702880}],\"creditCardInformation\":{\"creditCardNumber\":\"4542007043384801\",\"creditCardExpiry\":\"03/25\",\"cvv\":\"968\"}},{\"orderNo\":\"31BFAAD5\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":8000,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900},{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"2720038185043879\",\"creditCardExpiry\":\"09/24\",\"cvv\":\"087\"}},{\"orderNo\":\"0FB08E57\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":3600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100},{\"name\":\"R1: Margarita\",\"priceInPence\":1000}],\"creditCardInformation\":{\"creditCardNumber\":\"4451596397510242\",\"creditCardExpiry\":\"09/26\",\"cvv\":\"923\"}},{\"orderNo\":\"7CDB1796\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":1500,\"pizzasInOrder\":[{\"name\":\"R4: Proper Pizza\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"2221455521290424\",\"creditCardExpiry\":\"09/28\",\"cvv\":\"255\"}},{\"orderNo\":\"083EB18F\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4172964887520873\",\"creditCardExpiry\":\"08/25\",\"cvv\":\"557\"}},{\"orderNo\":\"473A5830\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4443489126468737\",\"creditCardExpiry\":\"02/27\",\"cvv\":\"058\"}},{\"orderNo\":\"4C35DF6B\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5480934687484848\",\"creditCardExpiry\":\"04/27\",\"cvv\":\"946\"}},{\"orderNo\":\"7560B624\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4987669839715746\",\"creditCardExpiry\":\"04/27\",\"cvv\":\"889\"}},{\"orderNo\":\"30D2BA0C\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4728600366788818\",\"creditCardExpiry\":\"04/25\",\"cvv\":\"897\"}},{\"orderNo\":\"4A6F61F7\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"5205340764353928\",\"creditCardExpiry\":\"08/24\",\"cvv\":\"974\"}},{\"orderNo\":\"70B9BC94\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5200805791291528\",\"creditCardExpiry\":\"02/26\",\"cvv\":\"370\"}},{\"orderNo\":\"3454A841\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4937415433710332\",\"creditCardExpiry\":\"11/28\",\"cvv\":\"866\"}},{\"orderNo\":\"7AA205F9\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4058436276640249\",\"creditCardExpiry\":\"05/26\",\"cvv\":\"782\"}},{\"orderNo\":\"6124FAE4\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4787457507865072\",\"creditCardExpiry\":\"04/24\",\"cvv\":\"433\"}},{\"orderNo\":\"6908CA4F\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4380852504197001\",\"creditCardExpiry\":\"07/24\",\"cvv\":\"443\"}},{\"orderNo\":\"7621529C\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5362373217188541\",\"creditCardExpiry\":\"06/28\",\"cvv\":\"015\"}},{\"orderNo\":\"178BF047\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4343628439372893\",\"creditCardExpiry\":\"09/27\",\"cvv\":\"597\"}},{\"orderNo\":\"619B4C8A\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"2221435223525384\",\"creditCardExpiry\":\"01/25\",\"cvv\":\"050\"}},{\"orderNo\":\"7240A671\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4741748826128076\",\"creditCardExpiry\":\"09/25\",\"cvv\":\"041\"}},{\"orderNo\":\"4060F93D\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5492384659501904\",\"creditCardExpiry\":\"07/27\",\"cvv\":\"899\"}},{\"orderNo\":\"615D18C4\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4965830818041852\",\"creditCardExpiry\":\"03/26\",\"cvv\":\"323\"}},{\"orderNo\":\"6915454C\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"5193977531162316\",\"creditCardExpiry\":\"03/26\",\"cvv\":\"144\"}},{\"orderNo\":\"526BBE42\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"2720454870588039\",\"creditCardExpiry\":\"08/27\",\"cvv\":\"257\"}},{\"orderNo\":\"47122CD8\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5460276503407981\",\"creditCardExpiry\":\"04/27\",\"cvv\":\"051\"}},{\"orderNo\":\"50A6BE5C\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"5198266326328968\",\"creditCardExpiry\":\"08/24\",\"cvv\":\"270\"}},{\"orderNo\":\"27959A95\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"5552603104677408\",\"creditCardExpiry\":\"10/25\",\"cvv\":\"980\"}},{\"orderNo\":\"67A9AF2D\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4240625249613700\",\"creditCardExpiry\":\"11/28\",\"cvv\":\"307\"}},{\"orderNo\":\"7E9564F8\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5419227156729793\",\"creditCardExpiry\":\"06/28\",\"cvv\":\"184\"}},{\"orderNo\":\"1A276458\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4632593959857279\",\"creditCardExpiry\":\"09/25\",\"cvv\":\"354\"}},{\"orderNo\":\"509287BC\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4692967221925303\",\"creditCardExpiry\":\"09/27\",\"cvv\":\"934\"}},{\"orderNo\":\"15774C27\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4708163734786604\",\"creditCardExpiry\":\"04/25\",\"cvv\":\"830\"}},{\"orderNo\":\"59225F67\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"2221053363040765\",\"creditCardExpiry\":\"01/26\",\"cvv\":\"793\"}},{\"orderNo\":\"06E6D38C\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4880507543382817\",\"creditCardExpiry\":\"01/25\",\"cvv\":\"458\"}},{\"orderNo\":\"6218488F\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4286860294655612\",\"creditCardExpiry\":\"02/28\",\"cvv\":\"937\"}},{\"orderNo\":\"21B98206\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4435286029810449\",\"creditCardExpiry\":\"02/27\",\"cvv\":\"290\"}},{\"orderNo\":\"7F2A88FD\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"2221968745691029\",\"creditCardExpiry\":\"03/24\",\"cvv\":\"602\"}},{\"orderNo\":\"6C995C83\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4418019809723404\",\"creditCardExpiry\":\"09/28\",\"cvv\":\"781\"}},{\"orderNo\":\"0893A81C\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"5574408866979644\",\"creditCardExpiry\":\"03/27\",\"cvv\":\"356\"}},{\"orderNo\":\"37CC1855\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4950121770136006\",\"creditCardExpiry\":\"03/27\",\"cvv\":\"528\"}},{\"orderNo\":\"28F774AE\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4156842442407820\",\"creditCardExpiry\":\"07/24\",\"cvv\":\"144\"}},{\"orderNo\":\"1E570D32\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"5335201014554124\",\"creditCardExpiry\":\"08/26\",\"cvv\":\"333\"}},{\"orderNo\":\"3D9444B7\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4131010179746733\",\"creditCardExpiry\":\"11/25\",\"cvv\":\"480\"}},{\"orderNo\":\"1A4DEB1F\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4733817419521474\",\"creditCardExpiry\":\"05/26\",\"cvv\":\"085\"}},{\"orderNo\":\"69CA0D44\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4213827437777551\",\"creditCardExpiry\":\"08/28\",\"cvv\":\"884\"}},{\"orderNo\":\"07269017\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4475063330092392\",\"creditCardExpiry\":\"03/28\",\"cvv\":\"349\"}},{\"orderNo\":\"041BD8B3\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"5379245926769806\",\"creditCardExpiry\":\"06/26\",\"cvv\":\"240\"}},{\"orderNo\":\"30957CBD\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4629676830624599\",\"creditCardExpiry\":\"07/25\",\"cvv\":\"100\"}},{\"orderNo\":\"1E00B87D\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4364124015479512\",\"creditCardExpiry\":\"11/24\",\"cvv\":\"758\"}},{\"orderNo\":\"75D8A4F3\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4343198994017192\",\"creditCardExpiry\":\"07/28\",\"cvv\":\"524\"}},{\"orderNo\":\"03C4C2FA\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"2221714619671162\",\"creditCardExpiry\":\"08/26\",\"cvv\":\"137\"}},{\"orderNo\":\"28279E9A\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5259224969502676\",\"creditCardExpiry\":\"07/26\",\"cvv\":\"944\"}},{\"orderNo\":\"42F66EDD\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5450731821848581\",\"creditCardExpiry\":\"08/25\",\"cvv\":\"836\"}},{\"orderNo\":\"0767F8E9\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4194916538213963\",\"creditCardExpiry\":\"01/28\",\"cvv\":\"786\"}},{\"orderNo\":\"7AE63D2F\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"5140323981685627\",\"creditCardExpiry\":\"02/24\",\"cvv\":\"698\"}}]";
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(jsonData, Order[].class);
    }

    private void assertOrdersMatchExpected(Order[] actualOrders, Order[] expectedOrders) {
        assertEquals(actualOrders.length, expectedOrders.length);

        for (int i = 0; i < actualOrders.length; i++) {
            assertOrderEquals(actualOrders[i], expectedOrders[i]);
        }
    }

    private void assertOrderEquals(Order actualOrder, Order expectedOrder) {
        assertEquals(actualOrder.getOrderNo(), expectedOrder.getOrderNo());
        assertEquals(actualOrder.getOrderDate(), expectedOrder.getOrderDate());
        assertEquals(actualOrder.getOrderStatus(), expectedOrder.getOrderStatus());
        assertEquals(actualOrder.getOrderValidationCode(), expectedOrder.getOrderValidationCode());
        assertEquals(actualOrder.getPriceTotalInPence(), expectedOrder.getPriceTotalInPence());

        assertPizzasMatch(actualOrder.getPizzasInOrder(), expectedOrder.getPizzasInOrder());
    }

    private void assertPizzasMatch(Pizza[] actualPizzas, Pizza[] expectedPizzas) {
        assertEquals(actualPizzas.length, expectedPizzas.length);

        for (int j = 0; j < actualPizzas.length; j++) {
            assertEquals(actualPizzas[j].name(), expectedPizzas[j].name());
            assertEquals(actualPizzas[j].priceInPence(), expectedPizzas[j].priceInPence());
        }
    }
    public void testServiceAlive() throws IOException, InterruptedException {
        String alive = RestService.isAlive(SERVICE_URL);
        assertEquals("Service should report as alive", "true", alive);
    }


    public void testServiceAliveWhenServiceDown() throws IOException, InterruptedException {
        try (MockedStatic<RestService> mockedStatic = Mockito.mockStatic(RestService.class)) {
            mockedStatic.when(() -> RestService.isAlive("SomeURL")).thenReturn("false");
            assertEquals("false", RestService.isAlive("SomeURL"));
        }
    }


    public void testServiceWrongURL() {
        try {
            RestService.isAlive("https://google.com/");
            fail("Service should throw exception");
        } catch (Exception e) {
        }
    }
}
