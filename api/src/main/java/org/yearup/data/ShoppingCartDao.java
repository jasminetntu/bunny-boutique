package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);
    ShoppingCart addToCart(int userId, int productId);
    ShoppingCart update(int userId, int productId, int quantity);
    ShoppingCart delete(int userId);
}
