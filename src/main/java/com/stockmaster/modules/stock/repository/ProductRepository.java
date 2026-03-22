package com.stockmaster.modules.stock.repository;

import com.stockmaster.common.enums.StockStatus;
import com.stockmaster.modules.stock.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCode(String productCode);

    Optional<Product> findByBarcode(String barcode);

    boolean existsByProductCode(String productCode);

    @Query("SELECT p FROM Product p WHERE p.deleted = false AND (:keyword IS NULL OR p.productCode LIKE %:keyword% OR p.productName LIKE %:keyword%) AND (:categoryId IS NULL OR p.categoryId = :categoryId) AND (:status IS NULL OR p.status = :status)")
    Page<Product> findByConditions(@Param("keyword") String keyword, @Param("categoryId") Long categoryId, @Param("status") StockStatus status, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.deleted = false AND p.categoryId = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.deleted = false")
    Long countActiveProducts();

    @Query("SELECT p FROM Product p WHERE p.deleted = false AND p.status = :status ORDER BY p.createTime DESC")
    List<Product> findByStatus(@Param("status") StockStatus status);
}
