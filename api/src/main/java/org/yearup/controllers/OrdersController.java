package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.models.*;

import java.security.Principal;
import java.util.Map;

/**
 * REST controller for managing endpoints for handling order-related operations.
 */
@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrdersController {
    private OrdersDao ordersDao;
    private OrderLineItemsDao orderLineItemsDao;
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProfileDao profileDao;

    /**
     * Constructs an {@code OrdersController} with required data access dependencies.
     *
     * @param ordersDao DAO for order persistence
     * @param orderLineItemsDao DAO for order line item persistence
     * @param shoppingCartDao DAO for shopping cart access
     * @param userDao DAO for user lookup
     * @param profileDao DAO for profile lookup
     */
    @Autowired
    public OrdersController(OrdersDao ordersDao, OrderLineItemsDao orderLineItemsDao,
                            ShoppingCartDao shoppingCartDao, UserDao userDao, ProfileDao profileDao) {
        this.ordersDao = ordersDao;
        this.orderLineItemsDao = orderLineItemsDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    /**
     * Creates a new order for the currently authenticated user.
     * The user's shopping cart is converted into an order and corresponding
     * order line items. The shopping cart is cleared after the order is created.
     *
     * @param principal provides the username of the currently authenticated user
     * @return the newly created {@link Order}
     */
    @PostMapping()
    public Order addOrder(Principal principal) {
        String userName = principal.getName(); // get currently logged in username
        User user = userDao.getByUserName(userName); // find db user by username
        int userId = user.getId(); // get userId

        Profile profile = profileDao.getByUserId(userId);
        ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);

        if (shoppingCart.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Cart cannot be empty when adding order.");
        }

        Order order = ordersDao.create(profile, shoppingCart.getTotal());
        int orderId = order.getOrderId();

        shoppingCart.getItems().forEach((productId, cartItem) ->
                orderLineItemsDao.create(orderId, productId, cartItem));

        shoppingCartDao.delete(userId);

        return order;
    }

}
