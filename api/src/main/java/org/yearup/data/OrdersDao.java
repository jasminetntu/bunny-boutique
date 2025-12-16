package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.Profile;

import java.math.BigDecimal;

public interface OrdersDao {
    Order getById(int orderId);
    Order create(Profile profile, BigDecimal total);
}
