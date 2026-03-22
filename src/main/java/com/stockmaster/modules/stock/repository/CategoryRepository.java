package com.stockmaster.modules.stock.repository;

import com.stockmaster.modules.stock.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryCode(String categoryCode);

    boolean existsByCategoryCode(String categoryCode);

    @Query("SELECT c FROM Category c WHERE c.deleted = false ORDER BY c.sortOrder")
    List<Category> findAllOrderBySortOrder();

    @Query("SELECT c FROM Category c WHERE c.deleted = false AND c.parentId = :parentId ORDER BY c.sortOrder")
    List<Category> findByParentId(@Param("parentId") Long parentId);

    @Query("SELECT c FROM Category c WHERE c.deleted = false AND c.status = 1 ORDER BY c.sortOrder")
    List<Category> findAllActive();

    @Query("SELECT COUNT(c) FROM Category c WHERE c.deleted = false")
    Long countActiveCategories();
}
