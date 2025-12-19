package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.Profile;

import java.math.BigDecimal;

/**
 * Data Access Object (DAO) interface for {@link Order} entities.
 * <p>
 * Defines methods for retrieving and creating orders in the database.
 * </p>
 */
public interface OrdersDao {
    Order getById(int orderId);
    Order create(Profile profile, BigDecimal total);
}
