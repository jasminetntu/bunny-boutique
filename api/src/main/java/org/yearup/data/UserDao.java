package org.yearup.data;

import org.yearup.models.User;

import java.util.List;

/**
 * Data Access Object (DAO) interface for {@link User} entities.
 * <p>
 * Defines methods for retrieving, creating, and checking the existence of users in the database.
 * </p>
 */
public interface UserDao {
    List<User> getAll();
    User getUserById(int userId);
    User getByUserName(String username);
    int getIdByUsername(String username);
    User create(User user);
    boolean exists(String username);
}
