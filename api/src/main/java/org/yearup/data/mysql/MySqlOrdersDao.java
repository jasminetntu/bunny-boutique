package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrdersDao;

import javax.sql.DataSource;

@Component
public class MySqlOrdersDao extends MySqlDaoBase implements OrdersDao {
    public MySqlOrdersDao(DataSource dataSource)
    {
        super(dataSource);
    }

    //todo implement methods
}
