package org.yearup.data;

import org.yearup.models.Product;

import java.math.BigDecimal;
import java.util.List;


/**
 * Data Access Object (DAO) interface for {@link Product} entities.
 * <p>
 * Defines methods for searching, retrieving, creating, updating, and deleting products in the database.
 * </p>
 */
public interface ProductDao
{
    List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String subCategory);
    List<Product> listByCategoryId(int categoryId);
    Product getById(int productId);
    Product create(Product product);
    void update(int productId, Product product);
    void delete(int productId);
}
