package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderLineItemsDao;
import org.yearup.data.OrdersDao;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MySQL implementation of the {@link OrderLineItemsDao} interface.
 * <p>
 * Provides methods to retrieve and create order line items in the database.
 * Each method interacts with the 'order_line_items' table.
 * </p>
 */
@Component
public class MySqlOrderLineItemsDao extends MySqlDaoBase implements OrderLineItemsDao {

    /**
     * Constructs a {@code MySqlOrderLineItemsDao} with the given {@link DataSource}.
     *
     * @param dataSource the {@link DataSource} used for database connections
     */
    public MySqlOrderLineItemsDao(DataSource dataSource)
    {
        super(dataSource);
    }

    /**
     * Retrieves an {@link OrderLineItem} by its ID.
     *
     * @param orderLineItemId the ID of the order line item
     * @return the {@link OrderLineItem} if found, or {@code null} if not found
     */
    @Override
    public OrderLineItem getById(int orderLineItemId) {
        String sql = """
                SELECT * FROM order_line_items
                WHERE order_line_item_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderLineItemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new OrderLineItem(rs.getInt("order_line_item_id"),
                            rs.getInt("order_id"), rs.getInt("product_id"),
                            rs.getBigDecimal("sales_price"), rs.getInt("quantity"),
                            rs.getBigDecimal("discount"));
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Creates a new {@link OrderLineItem} in the database for the specified order.
     *
     * @param orderId   the ID of the order
     * @param productId the ID of the product
     * @param cartItem  the {@link ShoppingCartItem} containing quantity, price, and discount
     * @return the created {@link OrderLineItem} with the generated ID, or {@code null} if creation failed
     */
    @Override
    public OrderLineItem create(int orderId, int productId, ShoppingCartItem cartItem) {
        String sql = """
                INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount)
                VALUES (?, ?, ?, ?, ?);""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setBigDecimal(3, cartItem.getLineTotal());
            ps.setInt(4, cartItem.getQuantity());
            ps.setBigDecimal(5, cartItem.getDiscountPercent());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int newId = keys.getInt(1);
                        return getById(newId);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
