package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;

import javax.sql.DataSource;
import java.sql.*;

/**
 * MySQL implementation of the {@link ProfileDao} interface.
 * <p>
 * Provides methods to retrieve, create, and update user profiles
 * in the 'profiles' table.
 * </p>
 */
@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao {

    /**
     * Constructs a {@code MySqlProfileDao} with the given {@link DataSource}.
     *
     * @param dataSource the {@link DataSource} used for database connections
     */
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }

    /**
     * Retrieves a {@link Profile} by the user's ID.
     *
     * @param userId the ID of the user
     * @return the {@link Profile} if found, or {@code null} if not found
     */
    @Override
    public Profile getByUserId(int userId) {
        String sql = """
                SELECT * FROM profiles
                WHERE user_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Profile(rs.getInt("user_id"), rs.getString("first_name"),
                            rs.getString("last_name"), rs.getString("phone"),
                            rs.getString("email"), rs.getString("address"),
                            rs.getString("city"), rs.getString("state"),
                            rs.getString("zip"));
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null; // if not found
    }

    /**
     * Creates a new {@link Profile} in the database.
     *
     * @param profile the {@link Profile} to create
     * @return the created {@link Profile}
     */
    @Override
    public Profile create(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing {@link Profile} in the database for the given user ID.
     *
     * @param userId  the ID of the user whose profile is being updated
     * @param profile the updated {@link Profile} data
     */
    @Override
    public void update(int userId, Profile profile) {
        String sql = """
                UPDATE profiles
                SET first_name = ?, last_name = ?, phone = ?, email = ?, address = ?, city = ?, state = ?, zip = ?
                WHERE user_id = ?;""";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profile.getFirstName());
            ps.setString(2, profile.getLastName());
            ps.setString(3, profile.getPhone());
            ps.setString(4, profile.getEmail());
            ps.setString(5, profile.getAddress());
            ps.setString(6, profile.getCity());
            ps.setString(7, profile.getState());
            ps.setString(8, profile.getZip());
            ps.setInt(9, userId);

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
