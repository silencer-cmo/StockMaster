package com.stockmaster.modules.purchase.repository;

import com.stockmaster.common.enums.OrderStatus;
import com.stockmaster.modules.purchase.entity.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    Optional<PurchaseOrder> findByOrderNo(String orderNo);

    @Query("SELECT o FROM PurchaseOrder o WHERE o.deleted = false AND (:keyword IS NULL OR o.orderNo LIKE %:keyword%) AND (:supplierId IS NULL OR o.supplierId = :supplierId) AND (:status IS NULL OR o.status = :status)")
    Page<PurchaseOrder> findByConditions(@Param("keyword") String keyword, @Param("supplierId") Long supplierId, @Param("status") OrderStatus status, Pageable pageable);

    @Query("SELECT COUNT(o) FROM PurchaseOrder o WHERE o.deleted = false AND o.status = :status")
    Long countByStatus(@Param("status") OrderStatus status);

    @Query("SELECT COUNT(o) FROM PurchaseOrder o WHERE o.deleted = false AND o.orderDate BETWEEN :startTime AND :endTime")
    Long countByTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT SUM(o.totalAmount) FROM PurchaseOrder o WHERE o.deleted = false AND o.orderDate BETWEEN :startTime AND :endTime")
    Double sumAmountBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT FUNCTION('DATE', o.orderDate) as date, COUNT(o) as count, SUM(o.totalAmount) as amount FROM PurchaseOrder o WHERE o.deleted = false AND o.orderDate BETWEEN :startTime AND :endTime GROUP BY FUNCTION('DATE', o.orderDate) ORDER BY date")
    List<Object[]> getDailyStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
