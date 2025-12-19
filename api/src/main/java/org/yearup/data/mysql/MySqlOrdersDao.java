package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrdersDao;
import org.yearup.models.Order;
import org.yearup.models.Profile;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

/**
 * MySQL implementation of the {@link OrdersDao} interface.
 * <p>
 * Provides methods to retrieve and create orders in the database.
 * Each method interacts with the 'orders' table.
 * </p>
 */
@Component
public class MySqlOrdersDao extends MySqlDaoBase implements OrdersDao {

    /**
     * Constructs a {@code MySqlOrdersDao} with the given {@link DataSource}.
     *
     * @param dataSource the {@link DataSource} used for database connections
     */
    public MySqlOrdersDao(DataSource dataSource)
    {
        super(dataSource);
    }

    /**
     * Retrieves an {@link Order} by its ID.
     *
     * @param orderId the ID of the order
     * @return the {@link Order} if found, or {@code null} if not found
     */
    @Override
    public Order getById(int orderId) {
        String sql = """
                SELECT * FROM orders
                WHERE order_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Order(rs.getInt("order_id"), rs.getInt("user_id"),
                            rs.getDate("date"), rs.getString("address"),
                            rs.getString("city"), rs.getString("state"),
                            rs.getString("zip"), rs.getBigDecimal("shipping_amount"));
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Creates a new {@link Order} in the database for the specified {@link Profile}.
     *
     * @param profile the {@link Profile} of the user placing the order
     * @param total   the total amount including shipping
     * @return the created {@link Order} with the generated ID, or {@code null} if creation failed
     */
    @Override
    public Order create(Profile profile, BigDecimal total) {
        String sql = """
                INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount)
                VALUES (?, ?, ?, ?, ?, ?, ?);""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, profile.getUserId());
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setString(3, profile.getAddress());
            ps.setString(4, profile.getCity());
            ps.setString(5, profile.getState());
            ps.setString(6, profile.getZip());
            ps.setBigDecimal(7, total);

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
