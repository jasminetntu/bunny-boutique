package org.yearup.data;

import org.yearup.models.Category;

import java.util.List;

/**
 * Data Access Object (DAO) interface for {@link Category} entities.
 * <p>
 * Defines methods for CRUD operations on categories.
 * Implementations will interact with the underlying database.
 * </p>
 */
public interface CategoryDao {
    List<Category> getAllCategories();
    Category getById(int categoryId);
    Category create(Category category);
    void update(int categoryId, Category category);
    void delete(int categoryId);
}
