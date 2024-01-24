package uk.ac.ed.inf;

import junit.framework.TestCase;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.provider.Arguments;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;


public class OrderValidatorTest extends TestCase {

    @RepeatedTest(10)
    public void testCreditCardNumberScenarios() {
        Order order;
        Restaurant restaurant;
        Order validatedOrder;
        String generatedCreditCardNumber;
        Random random = new Random();

        for (int i = 0; i < 3; i++) {
            order = createValidOrder();
            restaurant = createValidRestaurant();
            order = createValidPizza(restaurant, order);

            switch (i) {
                case 0: // Generate a random 16-digit credit card number with valid digits
                    StringBuilder creditCardNumberBuilder = new StringBuilder();
                    for (int j = 0; j < 16; j++) {
                        int randomDigit = random.nextInt(10); // digits from 0 to 9
                        creditCardNumberBuilder.append(randomDigit);
                    }
                    generatedCreditCardNumber = creditCardNumberBuilder.toString();
                    break;
                case 1: // Generate a 16-character string of random ASCII characters
                    generatedCreditCardNumber = random.ints(0, 128)
                            .limit(16)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();
                    break;
                case 2: // Empty credit card number
                    generatedCreditCardNumber = "";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + i);
            }

            order.getCreditCardInformation().setCreditCardNumber(generatedCreditCardNumber);

            validatedOrder = new OrderValidator().validateOrder(order, new Restaurant[]{restaurant});

            displayOrder(validatedOrder);

            assertEquals(OrderStatus.INVALID, validatedOrder.getOrderStatus());
            assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, validatedOrder.getOrderValidationCode());
        }
    }



    @RepeatedTest(100)
    public void testCreditCardCVVScenarios() {
        Order order;
        Restaurant restaurant;
        Order validatedOrder;
        String generatedString;
        int targetStringLength = 0;
        int leftLimit, rightLimit;

        for (int i = 0; i < 4; i++) {
            order = createValidOrder();
            restaurant = createValidRestaurant();
            order = createValidPizza(restaurant, order);

            if (i == 3) { // Case for empty CVV string
                generatedString = "";
            } else {
                if (i == 0) { // CVV with random ASCII characters of non-fixed length
                    leftLimit = 0;
                    rightLimit = 127;
                } else if (i == 1) { // CVV with numbers of non-fixed length
                    leftLimit = 48; // '0'
                    rightLimit = 57; // '9'
                } else { // i == 2, CVV with 3 random ASCII characters
                    leftLimit = 0;
                    rightLimit = 127;
                    targetStringLength = 3;
                }

                if (i != 2) { // For cases 0 and 1, ensure targetStringLength is not 3
                    do {
                        targetStringLength = (int)(Math.random() * 100);
                    } while (targetStringLength == 3);
                }

                generatedString = random.ints(leftLimit, rightLimit + 1)
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
            }

            order.getCreditCardInformation().setCvv(generatedString);

            validatedOrder = new OrderValidator().validateOrder(order, new Restaurant[]{restaurant});

            displayOrder(validatedOrder);

            assertEquals(OrderStatus.INVALID, validatedOrder.getOrderStatus());
            assertEquals(OrderValidationCode.CVV_INVALID, validatedOrder.getOrderValidationCode());
        }
    }




    @RepeatedTest(100)
    public void testCreditCardCVV3Numbers() {
        Order order = createValidOrder();

        int leftLimit = 48;
        int rightLimit = 57;
        int targetStringLength = 3;

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        order.getCreditCardInformation().setCvv(generatedString);

        Restaurant restaurant = createValidRestaurant();

        order = createValidPizza(restaurant, order);

        Order validatedOrder = new OrderValidator().validateOrder(order, new Restaurant[]{restaurant});

        displayOrder(validatedOrder);

        assertEquals(OrderStatus.VALID_BUT_NOT_DELIVERED, validatedOrder.getOrderStatus()) ;
        assertEquals(OrderValidationCode.NO_ERROR, validatedOrder.getOrderValidationCode());
    }


    @RepeatedTest(10) // Increased count to cover both scenarios
    public void testCreditCardExpiryDateScenarios() {
        Order order;
        Restaurant restaurant;
        Order validatedOrder;
        String generatedString;
        int currentYear = LocalDate.now().getYear() - 2000;

        for (int i = 0; i < 2; i++) {
            order = createValidOrder();
            restaurant = createValidRestaurant();

            order = createValidPizza(restaurant, order);

            if (i % 2 == 0) {
                // Scenario: Expiry date in the past
                int leftLimit = 48; // '0'
                int rightLimit = 57; // '9'
                int targetStringLength = 2;
                generatedString = random.ints(leftLimit, rightLimit + 1)
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                int randomYear = ThreadLocalRandom.current().nextInt(10, currentYear - 1);
                generatedString = generatedString + '/' + randomYear;
            } else {
                // Scenario: Invalid expiry date
                int randomMonth = random.nextInt(13, 99);
                int randomYear = random.nextInt(0, currentYear);
                if (randomYear < 10) {
                    generatedString = Integer.toString(randomMonth) + "/0" + randomYear;
                } else {
                    generatedString = Integer.toString(randomMonth) + '/' + randomYear;
                }
            }

            order.getCreditCardInformation().setCreditCardExpiry(generatedString);

            validatedOrder = new OrderValidator().validateOrder(order, new Restaurant[]{restaurant});

            displayOrder(validatedOrder);

            assertEquals(OrderStatus.INVALID, validatedOrder.getOrderStatus());
            assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, validatedOrder.getOrderValidationCode());
        }
    }


    @RepeatedTest(100)
    public void testCreditCardExpiryDateValid() {
        Order order = createOrderWithValidCreditCardExpiry();

        Restaurant restaurant = createValidRestaurant();
        order = addPizzaToOrder(order, restaurant);

        Order validatedOrder = validateOrder(order, restaurant);

        assertOrderValidButNotDelivered(validatedOrder);
    }

    private Order createOrderWithValidCreditCardExpiry() {
        Order order = createValidOrder();
        String expiryDate = generateValidCreditCardExpiryDate();
        order.getCreditCardInformation().setCreditCardExpiry(expiryDate);
        return order;
    }

    private String generateValidCreditCardExpiryDate() {
        int currentYear = LocalDate.now().getYear() % 100;
        int randomMonth = random.nextInt(1, 13);
        int randomYear = random.nextInt(currentYear + 1, 99);
        return String.format("%02d/%d", randomMonth, randomYear);
    }

    private Order addPizzaToOrder(Order order, Restaurant restaurant) {
        return createValidPizza(restaurant, order);
    }

    private Order validateOrder(Order order, Restaurant restaurant) {
        return new OrderValidator().validateOrder(order, new Restaurant[]{restaurant});
    }

    private void assertOrderValidButNotDelivered(Order validatedOrder) {
        displayOrder(validatedOrder);
        assertEquals(OrderStatus.VALID_BUT_NOT_DELIVERED, validatedOrder.getOrderStatus());
        assertEquals(OrderValidationCode.NO_ERROR, validatedOrder.getOrderValidationCode());
    }




    @RepeatedTest(100)
    public void testOrderTotalPriceScenarios() {
        Order order;
        Restaurant restaurant;
        Order validatedOrder;
        int randomNumber;

        for (int i = 0; i < 5; i++) {
            order = createValidOrder();
            restaurant = createValidRestaurant();

            for (int j = 0; j <= i / 2; j++) {
                order = createValidPizza(restaurant, order);
            }

            if (i % 5 == 0) {
                randomNumber = random.nextInt(-100000, 0);
            } else if (i % 5 == 1) {
                randomNumber = 0;
            } else if (i % 5 == 2) {
                randomNumber = order.getPriceTotalInPence() - SystemConstants.ORDER_CHARGE_IN_PENCE;
            } else {
                randomNumber = order.getPriceTotalInPence() + ((i % 5 == 3) ? SystemConstants.ORDER_CHARGE_IN_PENCE : SystemConstants.ORDER_CHARGE_IN_PENCE * 2);
            }

            order.setPriceTotalInPence(randomNumber);
            validatedOrder = new OrderValidator().validateOrder(order, new Restaurant[]{restaurant});

            displayOrder(validatedOrder);

            assertEquals(OrderStatus.INVALID, validatedOrder.getOrderStatus());
            assertEquals(OrderValidationCode.TOTAL_INCORRECT, validatedOrder.getOrderValidationCode());
        }
    }



    @RepeatedTest(100)
    public void testPizzaNotInRestaurant() {
        Order order = createValidOrder();
        Restaurant restaurantWithLimitedPizzaOptions = createLimitedPizzaRestaurant();
        Restaurant restaurantForOrder = createValidRestaurant();

        order = addPizzaToOrderFromRestaurant(restaurantForOrder, order);
        Order validatedOrder = validateOrderInDifferentRestaurant(order, restaurantWithLimitedPizzaOptions);

        displayOrder(validatedOrder);

        assertOrderInvalidDueToUndefinedPizza(validatedOrder);
    }

    private Restaurant createLimitedPizzaRestaurant() {
        return createRestaurant(
                "Restaurant :(",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.FRIDAY},
                new Pizza[] {new Pizza("Pizza", 2000)}
        );
    }

    private Order addPizzaToOrderFromRestaurant(Restaurant restaurant, Order order) {
        return createValidPizza(restaurant, order);
    }

    private Order validateOrderInDifferentRestaurant(Order order, Restaurant restaurant) {
        return new OrderValidator().validateOrder(order, new Restaurant[] {restaurant});
    }

    private void assertOrderInvalidDueToUndefinedPizza(Order order) {
        assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, order.getOrderValidationCode());
    }

    @RepeatedTest(100)
    public void testPizzasFromDifferentRestaurants() {
        Order order = createValidOrder();
        Restaurant[] restaurants = {createValidRestaurant(), createRestaurantWithLimitedAvailability()};

        order = addPizzasFromMultipleRestaurants(order, restaurants);

        Order validatedOrder = new OrderValidator().validateOrder(order, restaurants);

        displayOrder(validatedOrder);

        assertOrderInvalidDueToMultipleRestaurantSources(validatedOrder);
    }

    private Restaurant createRestaurantWithLimitedAvailability() {
        return createRestaurant(
                "Restaurant :(",
                new DayOfWeek[] {DayOfWeek.MONDAY, DayOfWeek.FRIDAY},
                new Pizza[] {new Pizza("Pizza", 2000)}
        );
    }

    private Order addPizzasFromMultipleRestaurants(Order order, Restaurant[] restaurants) {
        for (Restaurant restaurant : restaurants) {
            order = createValidPizza(restaurant, order);
        }
        return order;
    }

    private void assertOrderInvalidDueToMultipleRestaurantSources(Order order) {
        assertEquals(OrderStatus.INVALID, order.getOrderStatus());
        assertEquals(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS, order.getOrderValidationCode());
    }

    @RepeatedTest(100)
    public void testPizzasFromSameRestaurant() {
        Order order = createValidOrder();
        Restaurant restaurant1 = createValidRestaurant();
        Restaurant restaurant2 = createRestaurant(
                "Restaurant :(",
                new DayOfWeek[] {
                        DayOfWeek.MONDAY, DayOfWeek.FRIDAY
                },
                new Pizza[] {
                        new Pizza("Pizza", 2000)
                }
        );

        order = createValidPizza(restaurant1, order);
        order = createValidPizza(restaurant1, order);

        Order validatedOrder = new OrderValidator().validateOrder(order, new Restaurant[] { restaurant1, restaurant2 });

        displayOrder(validatedOrder);

        assertEquals(OrderStatus.VALID_BUT_NOT_DELIVERED, validatedOrder.getOrderStatus());
        assertEquals(OrderValidationCode.NO_ERROR, validatedOrder.getOrderValidationCode());
    }

    @RepeatedTest(100)
    public void test4PizzasInOneOrder() {
        Order order = createValidOrder();

        Restaurant restaurant = createValidRestaurant();

        for (int x = 0; x < 4; x++) {
            order = createValidPizza(restaurant, order);
        }

        Order validatedOrder = new OrderValidator().validateOrder(order, new Restaurant[] { restaurant });

        displayOrder(validatedOrder);

        assertEquals(OrderStatus.VALID_BUT_NOT_DELIVERED, validatedOrder.getOrderStatus());
        assertEquals(OrderValidationCode.NO_ERROR, validatedOrder.getOrderValidationCode());
    }



    @RepeatedTest(100)
    public void testOrderRestaurantClosed() {
        Order order = createValidOrder();
        Restaurant[] restaurants = {createValidRestaurant(), createClosedRestaurant()};

        order = addPizzaFromClosedRestaurant(order, restaurants[1]);

        Order validatedOrder = validateOrderWithClosedRestaurant(order, restaurants);

        displayAndAssertOrderInvalidDueToClosedRestaurant(validatedOrder);
    }

    private Restaurant createClosedRestaurant() {
        return createRestaurant(
                "Restaurant :(",
                new DayOfWeek[] { null },
                new Pizza[] { new Pizza("Pizza", 2000) }
        );
    }

    private Order addPizzaFromClosedRestaurant(Order order, Restaurant closedRestaurant) {
        return createValidPizza(closedRestaurant, order);
    }

    private Order validateOrderWithClosedRestaurant(Order order, Restaurant[] restaurants) {
        return new OrderValidator().validateOrder(order, restaurants);
    }

    private void displayAndAssertOrderInvalidDueToClosedRestaurant(Order validatedOrder) {
        displayOrder(validatedOrder);
        assertEquals(OrderStatus.INVALID, validatedOrder.getOrderStatus());
        assertEquals(OrderValidationCode.RESTAURANT_CLOSED, validatedOrder.getOrderValidationCode());
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Random random = new Random();
    public static Order createValidOrder() {
        var order = new Order();
        order.setOrderNo(String.format("%08X", ThreadLocalRandom.current().nextInt(1, Integer.MAX_VALUE)));
        order.setOrderDate(LocalDate.of(2023, 9, 1));

        order.setCreditCardInformation(
                new CreditCardInformation(
                        "0000000000000000",
                        String.format("%02d/%02d", ThreadLocalRandom.current().nextInt(1, 12), ThreadLocalRandom.current().nextInt(24, 30)),
                        "222"
                )
        );

        order.setOrderStatus(OrderStatus.UNDEFINED);
        order.setOrderValidationCode(OrderValidationCode.UNDEFINED);

        return order;
    }

    public static Restaurant createValidRestaurant() {
        // Creates valid restaurant
        return new Restaurant("testRestaurant",
                new LngLat(55.945535152517735, -3.1912869215011597),
                new DayOfWeek[] {
                        DayOfWeek.MONDAY, DayOfWeek.FRIDAY
                },
                new Pizza[]{
                        new Pizza("Pizza A", 2300),
                        new Pizza("Pizza B", 2400),
                        new Pizza("Pizza C", 2500)
                }
        ) ;
    }

    public static Restaurant createRestaurant(String name, DayOfWeek[] openOn, Pizza[] menu) {
        return new Restaurant(name,
                new LngLat(55.945535152517735, -3.1912869215011597),
                openOn,
                menu
        ) ;
    }

    public static Order createValidPizza(Restaurant restaurant, Order order) {
        ArrayList<Pizza> currentOrder = new ArrayList<>();
        Pizza[] pizzas = order.getPizzasInOrder();
        if (pizzas.length > 0) {
            Collections.addAll(currentOrder, pizzas);
        }

        int noPizzas = restaurant.menu().length;
        Pizza pizza = restaurant.menu()[random.nextInt(noPizzas)];
        int currentPrice = order.getPriceTotalInPence();
        currentOrder.add(pizza);

        Pizza[] newOrder = currentOrder.toArray(new Pizza[0]);
        order.setPizzasInOrder(newOrder);
        if (currentPrice == 0) {
            order.setPriceTotalInPence(pizza.priceInPence() + SystemConstants.ORDER_CHARGE_IN_PENCE);
        } else {
            order.setPriceTotalInPence(currentPrice + pizza.priceInPence());
        }

        return order;
    }

    public void displayOrder(Order order) {
        System.err.println("\n -- Order info \nOrderValidationCode: " +
                order.getOrderValidationCode() +
                "\nOrderStatus: " +
                order.getOrderStatus() +
                "\nOrderNo: " +
                order.getOrderNo() +
                "\nOrder Contents: " +
                Arrays.toString(order.getPizzasInOrder()) +
                "\nPriceTotalInPence: " +
                order.getPriceTotalInPence() +
                "\n -- Credit card info \nCreditCardNumber: " +
                order.getCreditCardInformation().getCreditCardNumber() +
                "\nCCV: " +
                order.getCreditCardInformation().getCvv() +
                "\nExpiryDate: " +
                order.getCreditCardInformation().getCreditCardExpiry()
        );
    }
}