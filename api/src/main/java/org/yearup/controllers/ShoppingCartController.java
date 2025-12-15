package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("cart")
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin
public class ShoppingCartController {
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    // NOTE: each method in this controller requires a Principal object as a parameter

    @GetMapping()
    public ShoppingCart getCart(Principal principal) {
        try {
            String userName = principal.getName(); // get currently logged in username
            User user = userDao.getByUserName(userName); // find db user by username
            int userId = user.getId(); // get userId

            // use the shoppingcartDao to get all items in the cart and return the cart
            return shoppingCartDao.getByUserId(userId);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when retrieving cart.");
        }
    }

    @PostMapping("products/{id}")
    public ShoppingCartItem addItem(Principal principal, @PathVariable int id) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.addToCart(userId, id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when adding to cart.");
        }
    }

    @PutMapping("products/{id}")
    public void updateItem(Principal principal, @PathVariable int id, @RequestBody ShoppingCartItem item) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.update(userId, id, item.getQuantity());
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when updating cart.");
        }
    }

    @DeleteMapping()
    public void deleteCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            shoppingCartDao.delete(userId);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when deleting cart.");
        }
    }

}
