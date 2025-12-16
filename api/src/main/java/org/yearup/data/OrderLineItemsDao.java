package org.yearup.data;

import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCartItem;

public interface OrderLineItemsDao {
    OrderLineItem getById(int orderLineItemId);
    OrderLineItem create(int orderId, int productId, ShoppingCartItem cartItem);
}
