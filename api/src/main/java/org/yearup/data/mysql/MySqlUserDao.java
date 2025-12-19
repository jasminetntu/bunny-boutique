package org.yearup.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yearup.data.UserDao;
import org.yearup.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * MySQL implementation of the {@link UserDao} interface.
 * <p>
 * Provides methods to create users, retrieve users by ID or username,
 * check for existence, and get a list of all users. Interacts with the 'users' table.
 * </p>
 */
@Component
public class MySqlUserDao extends MySqlDaoBase implements UserDao {

    /**
     * Constructs a {@code MySqlUserDao} with the given {@link DataSource}.
     *
     * @param dataSource the {@link DataSource} used for database connections
     */
    @Autowired
    public MySqlUserDao(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * Creates a new user in the database with a hashed password.
     *
     * @param newUser the {@link User} object containing username, password, and role
     * @return the created {@link User} with the password cleared
     */
    @Override
    public User create(User newUser) {
        String sql = "INSERT INTO users (username, hashed_password, role) VALUES (?, ?, ?)";
        String hashedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());

        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newUser.getUsername());
            ps.setString(2, hashedPassword);
            ps.setString(3, newUser.getRole());

            ps.executeUpdate();

            User user = getByUserName(newUser.getUsername());
            user.setPassword("");

            return user;

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a {@link List} of all {@link User} objects
     */
    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet row = statement.executeQuery();

            while (row.next()) {
                User user = mapRow(row);
                users.add(user);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return users;
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the ID of the user
     * @return the {@link User} object if found, or {@code null} if not found
     */
    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet row = statement.executeQuery();

            if (row.next()) {
                User user = mapRow(row);
                return user;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user
     * @return the {@link User} object if found, or {@code null} if not found
     */
    @Override
    public User getByUserName(String username) {
        String sql = "SELECT * " +
                " FROM users " +
                " WHERE username = ?";

        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet row = statement.executeQuery();

            if (row.next()) {
                User user = mapRow(row);
                return user;
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }

    /**
     * Retrieves the user ID for a given username.
     *
     * @param username the username of the user
     * @return the user ID if found, or -1 if the user does not exist
     */
    @Override
    public int getIdByUsername(String username) {
        User user = getByUserName(username);

        if (user != null) {
            return user.getId();
        }

        return -1;
    }

    /**
     * Checks if a user exists in the database by username.
     *
     * @param username the username to check
     * @return {@code true} if the user exists, {@code false} otherwise
     */
    @Override
    public boolean exists(String username) {
        User user = getByUserName(username);
        return user != null;
    }

    /**
     * Maps a {@link ResultSet} row to a {@link User} object.
     *
     * @param row the {@link ResultSet} row containing user data
     * @return a {@link User} object
     * @throws SQLException if a database access error occurs
     */
    private User mapRow(ResultSet row) throws SQLException {
        int userId = row.getInt("user_id");
        String username = row.getString("username");
        String hashedPassword = row.getString("hashed_password");
        String role = row.getString("role");

        return new User(userId, username,hashedPassword, role);
    }
}
