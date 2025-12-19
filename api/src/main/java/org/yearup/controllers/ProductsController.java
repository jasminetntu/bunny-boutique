package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for managing endpoints for handling product-related operations.
 */
@RestController
@RequestMapping("products")
@CrossOrigin
public class ProductsController
{
    private ProductDao productDao;

    /**
     * Constructs a {@code ProductsController} with the required data access object.
     *
     * @param productDao DAO used for product database operations
     */
    @Autowired
    public ProductsController(ProductDao productDao)
    {
        this.productDao = productDao;
    }

    /**
     * Searches for products using optional filter criteria.
     *
     * @param categoryId optional category ID to filter by
     * @param minPrice optional minimum price
     * @param maxPrice optional maximum price
     * @param subCategory optional sub-category filter
     * @return a list of products matching the search criteria
     */
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Product> search(@RequestParam(name="cat", required = false) Integer categoryId,
                                @RequestParam(name="minPrice", required = false) BigDecimal minPrice,
                                @RequestParam(name="maxPrice", required = false) BigDecimal maxPrice,
                                @RequestParam(name="subCategory", required = false) String subCategory
                                ) {
        try {
            return productDao.search(categoryId, minPrice, maxPrice, subCategory);
        }
        catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when searching for product.");
        }
    }

    /**
     * Retrieves a single product by its ID.
     *
     * @param id the product ID
     * @return the requested {@link Product}
     */
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Product getById(@PathVariable int id) {
        try {
            var product = productDao.getById(id);

            if (product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return product;
        }
        catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when retrieving product by id.");
        }
    }

    /**
     * Adds a new product to the system.
     *
     * @param product the product to add
     * @return the created {@link Product}
     */
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Product addProduct(@RequestBody Product product) {
        try {
            return productDao.create(product);
        }
        catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when adding product.");
        }
    }

    /**
     * Updates an existing product.
     *
     * @param id the ID of the product to update
     * @param product the updated product data
     */
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateProduct(@PathVariable int id, @RequestBody Product product) {
        try {
            productDao.update(id, product);
        }
        catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when updating product.");
        }
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProduct(@PathVariable int id) {
        try {
            var product = productDao.getById(id);

            if (product == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            productDao.delete(id);
        }
        catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when deleting product.");
        }
    }
}
