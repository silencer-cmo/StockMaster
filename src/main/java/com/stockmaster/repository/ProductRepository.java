package com.stockmaster.modules.stock.repository;

import com.stockmaster.modules.stock.entity.Product;
import com.stockmaster.common.enums.StockStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCode(String productCode);
    Page<Product> findByProductNameContainingOrProductCodeContaining(String nameKeyword, String codeKeyword, Pageable pageable);
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findByStatus(StockStatus status, Pageable pageable);
    List<Product> findByStatusIn(List<StockStatus> statusList);
    List<Product> findByQuantityLessThanEqual(Integer minStock);
    List<Product> findByQuantityGreaterThanEqual(Integer maxStock);
    Boolean existsByProductCode(String productCode);
}