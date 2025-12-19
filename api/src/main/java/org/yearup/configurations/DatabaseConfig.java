package org.yearup.configurations;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Configuration class responsible for setting up the application's database connection.
 */
@Configuration
public class DatabaseConfig
{
    private BasicDataSource basicDataSource;

    /**
     * Provides the configured {@link BasicDataSource} as a Spring-managed bean.
     *
     * @return the application's {@link BasicDataSource}
     */
    @Bean
    public BasicDataSource dataSource()
    {
        return basicDataSource;
    }

    /**
     * Constructs a {@code DatabaseConfig} and initializes the {@link BasicDataSource}
     * using values injected from the application's configuration properties.
     *
     * @param url the database connection URL
     * @param username the database username
     * @param password the database password
     */
    @Autowired
    public DatabaseConfig(@Value("${datasource.url}") String url,
                          @Value("${datasource.username}") String username,
                          @Value("${datasource.password}") String password)
    {
        basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(url);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
    }

}