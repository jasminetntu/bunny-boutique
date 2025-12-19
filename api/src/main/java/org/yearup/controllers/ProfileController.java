package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

/**
 * REST controller for managing endpoints for handling profile-related operations.
 */
@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {
    private ProfileDao profileDao;
    private UserDao userDao;

    /**
     * Constructs a {@code ProfileController} with required data access objects.
     *
     * @param profileDao DAO for profile-related operations
     * @param userDao DAO for user-related operations
     */
    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    /**
     * Retrieves the profile for the currently authenticated user.
     *
     * @param principal the security principal representing the logged-in user
     * @return the {@link Profile} associated with the current user
     */
    @GetMapping()
    public Profile getProfile(Principal principal) {
        try {
            String userName = principal.getName(); // get currently logged in username
            User user = userDao.getByUserName(userName); // find db user by username
            int userId = user.getId(); // get userId

            return profileDao.getByUserId(userId);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when retrieving profile.");
        }
    }

    /**
     * Updates the profile information for the currently authenticated user.
     *
     * @param principal the security principal representing the logged-in user
     * @param profile the updated profile data
     */
    @PutMapping()
    public void updateProfile(Principal principal, @RequestBody Profile profile) {
        try {
            String userName = principal.getName(); // get currently logged in username
            User user = userDao.getByUserName(userName); // find db user by username
            int userId = user.getId(); // get userId

            profileDao.update(userId, profile);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when retrieving profile.");
        }
    }
}
