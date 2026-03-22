package com.stockmaster.modules.stock.repository;

import com.stockmaster.modules.stock.entity.Inbound;
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
public interface InboundRepository extends JpaRepository<Inbound, Long> {

    Optional<Inbound> findByInboundNo(String inboundNo);

    @Query("SELECT i FROM Inbound i WHERE i.deleted = false AND (:keyword IS NULL OR i.inboundNo LIKE %:keyword%) AND (:productId IS NULL OR i.productId = :productId) AND (:supplierId IS NULL OR i.supplierId = :supplierId)")
    Page<Inbound> findByConditions(@Param("keyword") String keyword, @Param("productId") Long productId, @Param("supplierId") Long supplierId, Pageable pageable);

    @Query("SELECT COUNT(i) FROM Inbound i WHERE i.deleted = false AND i.inboundTime BETWEEN :startTime AND :endTime")
    Long countByTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT SUM(i.totalPrice) FROM Inbound i WHERE i.deleted = false AND i.inboundTime BETWEEN :startTime AND :endTime")
    Double sumTotalPriceBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT FUNCTION('DATE', i.inboundTime) as date, COUNT(i) as count, SUM(i.quantity) as quantity FROM Inbound i WHERE i.deleted = false AND i.inboundTime BETWEEN :startTime AND :endTime GROUP BY FUNCTION('DATE', i.inboundTime) ORDER BY date")
    List<Object[]> getDailyStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
