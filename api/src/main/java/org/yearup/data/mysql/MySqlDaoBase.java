package org.yearup.data.mysql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Base class for MySQL DAO implementations.
 * <p>
 * Provides shared database connection functionality using a {@link DataSource}.
 * </p>
 */
public abstract class MySqlDaoBase
{
    private DataSource dataSource;

    /**
     * Constructs the base DAO with the given data source.
     *
     * @param dataSource the JDBC data source
     */
    public MySqlDaoBase(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    /**
     * Obtains a new database connection.
     *
     * @return a {@link Connection} to the database
     * @throws SQLException if a connection cannot be obtained
     */
    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
