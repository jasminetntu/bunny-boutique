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

/**
 * REST controller for managing endpoints for handling shopping cart-related operations.
 */
@RestController
@RequestMapping("cart")
@PreAuthorize("hasRole('ROLE_USER')")
@CrossOrigin
public class ShoppingCartController {
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;

    /**
     * Constructs a {@code ShoppingCartController} with required dependencies.
     *
     * @param shoppingCartDao DAO for shopping cart operations
     * @param userDao DAO for user lookup
     * @param productDao DAO for product lookup
     */
    @Autowired
    public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao) {
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    /**
     * Retrieves the authenticated user's shopping cart.
     *
     * @param principal the currently authenticated user
     * @return the user's {@link ShoppingCart}
     */
    @GetMapping("")
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

    /**
     * Adds a product to the authenticated user's shopping cart.
     *
     * @param principal the currently authenticated user
     * @param id the ID of the product to add
     * @return the updated {@link ShoppingCart}
     */
    @PostMapping("products/{id}")
    public ShoppingCart addItem(Principal principal, @PathVariable int id) {
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

    /**
     * Updates the quantity of a product in the authenticated user's shopping cart.
     *
     * @param principal the currently authenticated user
     * @param id the ID of the product to update
     * @param item the shopping cart item containing the new quantity
     * @return the updated {@link ShoppingCart}
     */
    @PutMapping("products/{id}")
    public ShoppingCart updateItem(Principal principal, @PathVariable int id, @RequestBody ShoppingCartItem item) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.update(userId, id, item.getQuantity());
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when updating cart.");
        }
    }

    /**
     * Deletes all items from the authenticated user's shopping cart.
     *
     * @param principal the currently authenticated user
     * @return the emptied {@link ShoppingCart}
     */
    @DeleteMapping("")
    public ShoppingCart deleteCart(Principal principal) {
        try {
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            return shoppingCartDao.delete(userId);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when deleting cart.");
        }
    }

}
