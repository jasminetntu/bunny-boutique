package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.*;
import org.yearup.models.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("orders")
@CrossOrigin
public class OrdersController {
    private OrdersDao ordersDao;
    private OrderLineItemsDao orderLineItemsDao;
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProfileDao profileDao;

    @Autowired
    public OrdersController(OrdersDao ordersDao, OrderLineItemsDao orderLineItemsDao,
                            ShoppingCartDao shoppingCartDao, UserDao userDao, ProfileDao profileDao) {
        this.ordersDao = ordersDao;
        this.orderLineItemsDao = orderLineItemsDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    @PostMapping()
    public Order addOrder(Principal principal) {
        String userName = principal.getName(); // get currently logged in username
        User user = userDao.getByUserName(userName); // find db user by username
        int userId = user.getId(); // get userId

        Profile profile = profileDao.getByUserId(userId);
        ShoppingCart shoppingCart = shoppingCartDao.getByUserId(userId);

        Order order = ordersDao.create(profile, shoppingCart.getTotal());
        int orderId = order.getOrderId();

        shoppingCart.getItems().forEach((productId, cartItem) ->
                orderLineItemsDao.create(orderId, productId, cartItem));

        shoppingCartDao.delete(userId);

        return order;
    }

}
