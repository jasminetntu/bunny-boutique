package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL implementation of the {@link ProductDao} interface.
 * <p>
 * Provides methods to search, retrieve, create, update, and delete products
 * in the database. Interacts with the 'products' table.
 * </p>
 */
@Component
public class MySqlProductDao extends MySqlDaoBase implements ProductDao {

    /**
     * Constructs a {@code MySqlProductDao} with the given {@link DataSource}.
     *
     * @param dataSource the {@link DataSource} used for database connections
     */
    public MySqlProductDao(DataSource dataSource)
    {
        super(dataSource);
    }

    /**
     * Searches for products based on category, price range, and subcategory.
     *
     * @param categoryId the category ID to filter by, or {@code null} for no filter
     * @param minPrice   the minimum price, or {@code null} for no minimum
     * @param maxPrice   the maximum price, or {@code null} for no maximum
     * @param subCategory the subcategory to filter by, or {@code null} for no filter
     * @return a list of products matching the search criteria
     */
    @Override
    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String subCategory) {
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT * FROM products
                WHERE (category_id = ? OR ? = -1)
                   AND (price >= ? OR ? = -1)
                   AND (price <= ? OR ? = -1)
                   AND (subcategory = ? OR ? = '');""";

        categoryId = categoryId == null ? -1 : categoryId;
        minPrice = minPrice == null ? new BigDecimal("-1") : minPrice;
        maxPrice = maxPrice == null ? new BigDecimal("-1") : maxPrice;
        subCategory = subCategory == null ? "" : subCategory;

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            statement.setInt(2, categoryId);
            statement.setBigDecimal(3, minPrice);
            statement.setBigDecimal(4, minPrice);
            statement.setBigDecimal(5, maxPrice);
            statement.setBigDecimal(6, maxPrice);
            statement.setString(7, subCategory);
            statement.setString(8, subCategory);

            ResultSet row = statement.executeQuery();

            while (row.next()) {
                Product product = mapRow(row);
                products.add(product);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    /**
     * Retrieves all products in a specific category.
     *
     * @param categoryId the category ID
     * @return a list of products in the category
     */
    @Override
    public List<Product> listByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();

        String sql = """
                    SELECT * FROM products
                    WHERE category_id = ?;""";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);

            ResultSet row = statement.executeQuery();

            while (row.next()) {
                Product product = mapRow(row);
                products.add(product);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId the product ID
     * @return the {@link Product} if found, or {@code null} if not found
     */
    @Override
    public Product getById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?;";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);

            ResultSet row = statement.executeQuery();

            if (row.next()) {
                return mapRow(row);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Creates a new product in the database.
     *
     * @param product the product to create
     * @return the created {@link Product} with the generated ID, or {@code null} if creation failed
     */
    @Override
    public Product create(Product product) {
        String sql = """
                INSERT INTO products
                (name, price, category_id, description, subcategory, image_url, stock, featured)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);""";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getSubCategory());
            statement.setString(6, product.getImageUrl());
            statement.setInt(7, product.getStock());
            statement.setBoolean(8, product.isFeatured());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int orderId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    return getById(orderId);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Updates an existing product in the database.
     *
     * @param productId the ID of the product to update
     * @param product   the updated product data
     */
    @Override
    public void update(int productId, Product product) {
        String sql = """
                UPDATE products
                SET name = ?,
                    price = ?,
                    category_id = ?,
                    description = ?,
                    subcategory = ?,
                    image_url = ?,
                    stock = ?,
                    featured = ?
                WHERE product_id = ?;""";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getSubCategory());
            statement.setString(6, product.getImageUrl());
            statement.setInt(7, product.getStock());
            statement.setBoolean(8, product.isFeatured());
            statement.setInt(9, productId);

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a product from the database by ID.
     *
     * @param productId the ID of the product to delete
     */
    @Override
    public void delete(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?;";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, productId);

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Maps a row from the {@link ResultSet} to a {@link Product} object.
     *
     * @param row the {@link ResultSet} positioned at the current row
     * @return a {@link Product} object with data from the row
     * @throws SQLException if an SQL error occurs
     */
    protected static Product mapRow(ResultSet row) throws SQLException {
        int productId = row.getInt("product_id");
        String name = row.getString("name");
        BigDecimal price = row.getBigDecimal("price");
        int categoryId = row.getInt("category_id");
        String description = row.getString("description");
        String subCategory = row.getString("subcategory");
        int stock = row.getInt("stock");
        boolean isFeatured = row.getBoolean("featured");
        String imageUrl = row.getString("image_url");

        return new Product(productId, name, price, categoryId, description, subCategory, stock, isFeatured, imageUrl);
    }
}
