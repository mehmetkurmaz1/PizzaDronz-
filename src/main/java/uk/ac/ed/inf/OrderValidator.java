package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.constant.SystemConstants;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Class for validating pizza orders.
 */
public class OrderValidator implements OrderValidation {
    /**
     * Validates an order based on several criteria such as credit card information,
     * pizza count, total cost, and availability in restaurants.
     *
     * @param orderToValidate The order to be validated.
     * @param definedRestaurants The list of available restaurants.
     * @return The validated order with updated status and validation code.
     */
    @Override
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {
        // Retrieve pizzas in order and credit card information
        Pizza[] pizzaList = orderToValidate.getPizzasInOrder();
        CreditCardInformation cardInformation = orderToValidate.getCreditCardInformation();

        // Check if the number of pizzas exceeds the maximum allowed
        if (pizzaList.length > SystemConstants.MAX_PIZZAS_PER_ORDER){
            orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Validate the CVV of the credit card (must be a 3 digit number)
        if (!cardInformation.getCvv().matches("[0-9]{3}")){
            orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Validate the credit card number using Luhn's algorithm
        String cardNumber = cardInformation.getCreditCardNumber();
        if (!cardNumber.matches("[0-9]{16}")) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }
        int nDigits = cardNumber.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = cardNumber.charAt(i) - '0';
            if (isSecond) d *= 2;
            nSum += d / 10;
            nSum += d % 10;
            isSecond = !isSecond;
        }
        if (nSum % 10 != 0) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Check if the credit card has expired
        String cardExpiry = cardInformation.getCreditCardExpiry();
        int month = Integer.parseInt(cardExpiry.substring(0, 2));
        int year = Integer.parseInt("20" + cardExpiry.substring(3, 5));
        if (month > 11) {
            month = 1;
            year += 1;
        } else {
            month += 1;
        }
        LocalDate expiryDate = LocalDate.of(year, month, 1);
        if (!orderToValidate.getOrderDate().isBefore(expiryDate)) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Calculate and validate the total cost of the order
        double totalCost = Arrays.stream(pizzaList).mapToDouble(Pizza::priceInPence).sum();
        totalCost += SystemConstants.ORDER_CHARGE_IN_PENCE;
        if (orderToValidate.getPriceTotalInPence() != totalCost) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // Check if the ordered pizzas are available in the defined restaurants
        int counter = 0;
        Restaurant currRestaurant = null;
        for (Restaurant restaurant : definedRestaurants) {
            long intersectionLength = Arrays.stream(pizzaList).filter(p -> Arrays.asList(restaurant.menu()).contains(p)).count();
            if (intersectionLength == pizzaList.length) {
                counter++;
                currRestaurant = restaurant;
            } else if (intersectionLength > 0) {
                counter++;
            }
        }
        if (counter > 1) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        } else if (currRestaurant == null) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        } else if (!Arrays.asList(currRestaurant.openingDays()).contains(orderToValidate.getOrderDate().getDayOfWeek())) {
            orderToValidate.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        // If all validations pass, set the order status as valid but not yet delivered
        orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        orderToValidate.setOrderStatus(OrderStatus.VALID_BUT_NOT_DELIVERED);
        return orderToValidate;
    }
}
