package com.stockmaster.modules.system.repository;

import com.stockmaster.modules.system.entity.SysLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SysLogRepository extends JpaRepository<SysLog, Long> {

    @Query("SELECT l FROM SysLog l WHERE (:operationType IS NULL OR l.operationType = :operationType) AND (:module IS NULL OR l.module LIKE %:module%) AND (:username IS NULL OR l.username LIKE %:username%) AND (:status IS NULL OR l.status = :status) AND (:startTime IS NULL OR l.createTime >= :startTime) AND (:endTime IS NULL OR l.createTime <= :endTime)")
    Page<SysLog> findByConditions(@Param("operationType") String operationType, @Param("module") String module, @Param("username") String username, @Param("status") Integer status, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, Pageable pageable);

    @Modifying
    @Query("DELETE FROM SysLog l WHERE l.createTime < :beforeTime")
    int deleteByCreateTimeBefore(@Param("beforeTime") LocalDateTime beforeTime);
}
