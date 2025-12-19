package org.yearup.data;

import org.yearup.models.Profile;

/**
 * Data Access Object (DAO) interface for {@link Profile} entities.
 * <p>
 * Defines methods for retrieving, creating, and updating user profiles in the database.
 * </p>
 */
public interface ProfileDao {
    Profile getByUserId(int userId);
    Profile create(Profile profile);
    void update(int userId, Profile profile);
}
