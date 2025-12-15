package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);
    ShoppingCartItem addToCart(int userId, int productId);
    void update(int userId, int productId, int quantity);
    void delete(int userId);
}
