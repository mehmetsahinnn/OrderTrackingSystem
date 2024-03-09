package com.github.mehmetsahinnn.onlineordertrackingsystem.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByCategoryAndPriceBetween(String category, Double minPrice, Double maxPrice);
    List<Product> findByCategoryAndPriceGreaterThan(String category, Double minPrice);
    List<Product> findByCategoryAndPriceLessThan(String category, Double maxPrice);
    List<Product> findByPriceGreaterThan(Double minPrice);
    List<Product> findByPriceLessThan(Double maxPrice);

}
