package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderLineItemsDao;
import org.yearup.data.OrdersDao;

import javax.sql.DataSource;

@Component
public class MySqlOrderLineItemsDao extends MySqlDaoBase implements OrderLineItemsDao {
    public MySqlOrderLineItemsDao(DataSource dataSource)
    {
        super(dataSource);
    }

    //todo implement methods
}
