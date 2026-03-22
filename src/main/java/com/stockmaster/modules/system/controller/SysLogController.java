package com.stockmaster.modules.system.controller;

import com.stockmaster.common.aop.LogOperation;
import com.stockmaster.common.dto.ApiResponse;
import com.stockmaster.common.dto.PageResult;
import com.stockmaster.common.enums.OperationType;
import com.stockmaster.modules.system.entity.SysLog;
import com.stockmaster.modules.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/system/logs")
@RequiredArgsConstructor
public class SysLogController {

    private final SysLogService sysLogService;

    @GetMapping
    @LogOperation(value = OperationType.QUERY, module = "日志管理", description = "查询日志列表")
    public ApiResponse<PageResult<SysLog>> getLogList(
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<SysLog> result = sysLogService.getLogList(operationType, module, username, status, startTime, endTime, pageNum, pageSize);
        return ApiResponse.success(result);
    }

    @DeleteMapping("/clear")
    @LogOperation(value = OperationType.DELETE, module = "日志管理", description = "清空日志")
    public ApiResponse<Void> clearLogs(@RequestParam(defaultValue = "30") Integer days) {
        sysLogService.clearLogs(days);
        return ApiResponse.success();
    }

    @DeleteMapping("/batch")
    @LogOperation(value = OperationType.DELETE, module = "日志管理", description = "批量删除日志")
    public ApiResponse<Void> deleteByIds(@RequestBody List<Long> ids) {
        sysLogService.deleteByIds(ids);
        return ApiResponse.success();
    }
}
