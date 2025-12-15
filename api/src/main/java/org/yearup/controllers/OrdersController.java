package org.yearup.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yearup.data.OrdersDao;
import org.yearup.data.ShoppingCartDao;

@RestController
@RequestMapping("order")
@CrossOrigin
public class OrdersController {
    private OrdersDao ordersDao;
    private OrderLineItemsDao orderLineItemsDao;
    private ShoppingCartDao shoppingCartDao;
}
