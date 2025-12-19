package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

/**
 * REST controller for managing endpoints for product categories and their associated products.
 */
@RestController
@RequestMapping("categories") // base url
@CrossOrigin
public class CategoriesController {
    private CategoryDao categoryDao;
    private ProductDao productDao;

    /**
     * Constructs a {@code CategoriesController} with required data access objects.
     *
     * @param categoryDao DAO for category-related database operations
     * @param productDao DAO for product-related database operations
     */
    @Autowired
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    /**
     * Retrieves all categories.
     *
     * @return a list of all {@link Category} objects
     */
    @GetMapping("")
    @PreAuthorize("permitAll()")
    public List<Category> getAll() {
        try {
            return categoryDao.getAllCategories();
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when retrieving all categories.");
        }
    }

    /**
     * Retrieves a category by its unique identifier.
     *
     * @param id the ID of the category
     * @return the {@link Category} with the specified ID
     */
    @GetMapping("{id}")
    @PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id) {
        try {
            var category = categoryDao.getById(id);

            if (category == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            return category;
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when retrieving category by id.");
        }
    }

    /**
     * Retrieves all products associated with a specific category.
     *
     * @param categoryId the ID of the category
     * @return a list of {@link Product} objects belonging to the category
     */
    @GetMapping("{categoryId}/products")
    @PreAuthorize("permitAll()")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        try {
            return productDao.listByCategoryId(categoryId);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when retrieving products by category id.");
        }
    }

    /**
     * Creates a new category. Restricted to users with the ADMIN role.
     *
     * @param category the category to create
     * @return the newly created {@link Category}
     */
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Category addCategory(@RequestBody Category category) {
        try {
            return categoryDao.create(category);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when adding category.");
        }
    }

    /**
     * Updates an existing category. Restricted to users with the ADMIN role.
     *
     * @param id the ID of the category to update
     * @param category the updated category data
     */
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCategory(@PathVariable int id, @RequestBody Category category) {
        try {
            categoryDao.update(id, category);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when updating category.");
        }
    }

    /**
     * Deletes a category by its ID. Restricted to users with the ADMIN role.
     *
     * @param id the ID of the category to delete
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable int id) {
        try {
            categoryDao.delete(id);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR: Internal server error when deleting category.");
        }
    }
}
