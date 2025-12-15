package org.yearup.data;

import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao {
    ShoppingCart getByUserId(int userId);

    // add additional method signatures here
    ShoppingCartItem addToCart(int userId, Product product);
    void update(int userId, Product product, int quantity);
    void delete(int userId);
}
