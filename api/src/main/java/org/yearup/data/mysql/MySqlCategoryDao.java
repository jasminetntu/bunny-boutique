package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL implementation of the {@link CategoryDao} interface.
 * <p>
 * This DAO handles all database operations related to {@link Category}
 * entities using JDBC and a MySQL data source.
 * </p>
 */
@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {

    /**
     * Constructs a {@code MySqlCategoryDao} with the provided data source.
     *
     * @param dataSource the data source used to obtain database connections
     */
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return a list of all {@link Category} records
     */
    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT * FROM categories;";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category category = mapRow(rs);
                categories.add(category);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param categoryId the ID of the category
     * @return the matching {@link Category}, or {@code null} if not found
     */
    @Override
    public Category getById(int categoryId) {
        String sql = """
                SELECT * FROM categories
                WHERE category_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null; // if not found
    }

    /**
     * Creates a new category in the database.
     *
     * @param category the category to create
     * @return the newly created {@link Category} with its generated ID
     */
    @Override
    public Category create(Category category) {
        String sql = """
                INSERT INTO categories (name, description)
                VALUES (?, ?);""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // retrieve generated keys
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int newId = keys.getInt(1); // retrieve auto-incremented ID
                        return getById(newId); // get newly inserted category
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Updates an existing category in the database.
     *
     * @param categoryId the ID of the category to update
     * @param category the updated category data
     */
    @Override
    public void update(int categoryId, Category category) {
        String sql = """
                UPDATE categories
                SET name = ?, description = ?
                WHERE category_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, categoryId);

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a category from the database by ID.
     *
     * @param categoryId the ID of the category to delete
     */
    @Override
    public void delete(int categoryId) {
        String sql = """
                DELETE FROM categories
                WHERE category_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Maps a single database row to a {@link Category} object.
     *
     * @param row the {@link ResultSet} containing category data
     * @return a populated {@link Category} instance
     * @throws SQLException if a database access error occurs
     */
    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
