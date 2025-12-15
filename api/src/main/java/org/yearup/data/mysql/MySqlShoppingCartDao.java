package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    //todo: implement methods

    public ShoppingCart getByUserId(int userId) {
        ShoppingCart cart = new ShoppingCart();

        String sql = """
                SELECT sc.product_id, sc.quantity, p.name, p.price, p.category_id, p.description,
                    p.subcategory, p.image_url, p.stock, p.featured
                FROM shopping_cart as sc
                INNER JOIN products as p
                    ON sc.product_id = p.product_id
                WHERE user_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(rs.getInt("product_id"),
                            rs.getString("name"), rs.getBigDecimal("price"),
                            rs.getInt("category_id"), rs.getString("description"),
                            rs.getString("subcategory"), rs.getInt("stock"),
                            rs.getBoolean("featured"), rs.getString("image_url"));

                    ShoppingCartItem cartItem = new ShoppingCartItem();
                    cartItem.setProduct(product);
                    cartItem.setQuantity(rs.getInt("quantity"));

                    cart.add(cartItem);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cart;
    }

    public ShoppingCartItem addToCart(int userId, Product product) {
        //todo
        return null;
    }

    public void update(int userId, Product product, int quantity) {
        //todo
    }

    public void delete(int userId) {
        //todo
    }
}
