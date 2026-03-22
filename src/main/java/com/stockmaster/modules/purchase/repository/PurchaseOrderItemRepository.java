package com.stockmaster.modules.purchase.repository;

import com.stockmaster.modules.purchase.entity.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    List<PurchaseOrderItem> findByOrderId(Long orderId);

    @Modifying
    @Query("DELETE FROM PurchaseOrderItem i WHERE i.orderId = :orderId")
    void deleteByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT SUM(i.quantity) FROM PurchaseOrderItem i WHERE i.orderId = :orderId")
    Integer sumQuantityByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT SUM(i.receivedQuantity) FROM PurchaseOrderItem i WHERE i.orderId = :orderId")
    Integer sumReceivedQuantityByOrderId(@Param("orderId") Long orderId);
}
