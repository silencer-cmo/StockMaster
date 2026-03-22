package com.stockmaster.modules.purchase.repository;

import com.stockmaster.modules.purchase.entity.SupplierEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface SupplierEvaluationRepository extends JpaRepository<SupplierEvaluation, Long> {

    List<SupplierEvaluation> findBySupplierId(Long supplierId);

    @Query("SELECT e FROM SupplierEvaluation e WHERE e.deleted = false AND e.supplierId = :supplierId")
    Page<SupplierEvaluation> findBySupplierId(@Param("supplierId") Long supplierId, Pageable pageable);

    @Query("SELECT COUNT(e) FROM SupplierEvaluation e WHERE e.deleted = false AND e.supplierId = :supplierId")
    Long countBySupplierId(@Param("supplierId") Long supplierId);

    @Query("SELECT AVG(e.qualityScore) FROM SupplierEvaluation e WHERE e.deleted = false AND e.supplierId = :supplierId")
    BigDecimal avgQualityScoreBySupplierId(@Param("supplierId") Long supplierId);

    @Query("SELECT AVG(e.deliveryScore) FROM SupplierEvaluation e WHERE e.deleted = false AND e.supplierId = :supplierId")
    BigDecimal avgDeliveryScoreBySupplierId(@Param("supplierId") Long supplierId);

    @Query("SELECT AVG(e.serviceScore) FROM SupplierEvaluation e WHERE e.deleted = false AND e.supplierId = :supplierId")
    BigDecimal avgServiceScoreBySupplierId(@Param("supplierId") Long supplierId);

    @Query("SELECT AVG(e.priceScore) FROM SupplierEvaluation e WHERE e.deleted = false AND e.supplierId = :supplierId")
    BigDecimal avgPriceScoreBySupplierId(@Param("supplierId") Long supplierId);

    @Query("SELECT AVG(e.totalScore) FROM SupplierEvaluation e WHERE e.deleted = false AND e.supplierId = :supplierId")
    BigDecimal avgTotalScoreBySupplierId(@Param("supplierId") Long supplierId);
}
