package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MySQL implementation of the {@link ShoppingCartDao} interface.
 * <p>
 * Provides methods to retrieve, add, update, and delete items in a user's shopping cart.
 * Interacts with the 'shopping_cart' and 'products' tables.
 * </p>
 */
@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {

    /**
     * Constructs a {@code MySqlShoppingCartDao} with the given {@link DataSource}.
     *
     * @param dataSource the {@link DataSource} used for database connections
     */
    @Autowired
    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * Retrieves the shopping cart for a specific user.
     *
     * @param userId the ID of the user
     * @return a {@link ShoppingCart} containing all items for the user
     */
    @Override
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

    /**
     * Adds a product to the user's shopping cart.
     * <p>
     * If the product is already in the cart, increments its quantity by 1.
     * </p>
     *
     * @param userId    the ID of the user
     * @param productId the ID of the product to add
     * @return the updated {@link ShoppingCart} for the user
     */
    @Override
    public ShoppingCart addToCart(int userId, int productId) {
        String sql = """
                INSERT INTO shopping_cart (user_id, product_id, quantity)
                VALUES (?, ?, ?);""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ShoppingCartItem cartItem = getItemById(userId, productId);

            if (cartItem == null) { // if item NOT in cart yet, add to cart w/ quantity 1
                ps.setInt(1, userId);
                ps.setInt(2, productId);
                ps.setInt(3, 1);

                ps.executeUpdate();
            }
            else { // if item ALREADY in cart, update quantity + 1
                update(userId, productId, cartItem.getQuantity() + 1);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return getByUserId(userId);
    }

    /**
     * Updates the quantity of a product in the user's shopping cart.
     * If the quantity <= 0, the item is removed from the cart.
     *
     * @param userId    the ID of the user
     * @param productId the ID of the product to update
     * @param quantity  the new quantity
     * @return the updated {@link ShoppingCart} for the user
     */
    @Override
    public ShoppingCart update(int userId, int productId, int quantity) {
        String sql = """
                UPDATE shopping_cart
                SET quantity = ?
                WHERE user_id = ? AND product_id = ?;""";

        if (getItemById(userId, productId) == null) { // if item NOT in cart
            throw new RuntimeException("ERROR: Cannot update quantity of an item not in cart yet.");
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (quantity > 0) { // update quantity
                ps.setInt(1, quantity);
                ps.setInt(2, userId);
                ps.setInt(3, productId);

                ps.executeUpdate();
            }
            else { // delete item if given quantity is 0 or less
                delete(userId, productId);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return getByUserId(userId);
    }

    /**
     * Deletes all items from the user's shopping cart.
     *
     * @param userId the ID of the user
     * @return an empty {@link ShoppingCart} for the user
     */
    @Override
    public ShoppingCart delete(int userId) {
        String sql = """
                DELETE FROM shopping_cart
                WHERE user_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return getByUserId(userId);
    }

    // ------------------------------------
    //            HELPER METHODS
    // ------------------------------------

    /**
     * Retrieves a single {@link ShoppingCartItem} by user ID and product ID.
     *
     * @param userId    the ID of the user
     * @param productId the ID of the product
     * @return the {@link ShoppingCartItem} if found, or {@code null} if not found
     */
    private ShoppingCartItem getItemById(int userId, int productId) {
        String sql = """
                SELECT sc.product_id, sc.quantity, p.name, p.price, p.category_id, p.description,
                    p.subcategory, p.image_url, p.stock, p.featured
                FROM shopping_cart as sc
                INNER JOIN products as p
                    ON sc.product_id = p.product_id
                WHERE user_id = ? AND sc.product_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product(rs.getInt("product_id"),
                            rs.getString("name"), rs.getBigDecimal("price"),
                            rs.getInt("category_id"), rs.getString("description"),
                            rs.getString("subcategory"), rs.getInt("stock"),
                            rs.getBoolean("featured"), rs.getString("image_url"));

                    ShoppingCartItem cartItem = new ShoppingCartItem();
                    cartItem.setProduct(product);
                    cartItem.setQuantity(rs.getInt("quantity"));

                    return cartItem;
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null; // if item not found
    }

    /**
     * Deletes a single product from the user's shopping cart.
     *
     * @param userId    the ID of the user
     * @param productId the ID of the product to delete
     */
    private void delete(int userId, int productId) {
        String sql = """
                DELETE FROM shopping_cart
                WHERE user_id = ? AND product_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
