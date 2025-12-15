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

@RestController
@RequestMapping("profile")
@CrossOrigin
public class ProfileController {
    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

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

    @PutMapping()
    public void updateProfile(@RequestBody Profile profile) {
        //todo
    }
}
