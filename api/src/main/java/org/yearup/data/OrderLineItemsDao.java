package org.yearup.data;

import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCartItem;

/**
 * Data Access Object (DAO) interface for {@link OrderLineItem} entities.
 * <p>
 * Defines methods for retrieving and creating order line items in the database.
 * </p>
 */
public interface OrderLineItemsDao {
    OrderLineItem getById(int orderLineItemId);
    OrderLineItem create(int orderId, int productId, ShoppingCartItem cartItem);
}
