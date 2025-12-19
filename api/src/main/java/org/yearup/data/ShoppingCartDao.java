package org.yearup.data;

import org.yearup.models.ShoppingCart;

/**
 * Data Access Object (DAO) interface for {@link ShoppingCart} entities.
 * <p>
 * Defines methods for retrieving, adding, updating, and deleting items in a user's shopping cart.
 * </p>
 */
public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);
    ShoppingCart addToCart(int userId, int productId);
    ShoppingCart update(int userId, int productId, int quantity);
    ShoppingCart delete(int userId);
}
