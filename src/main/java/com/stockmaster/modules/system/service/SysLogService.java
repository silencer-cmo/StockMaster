package com.stockmaster.modules.system.service;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.modules.system.entity.SysLog;

import java.time.LocalDateTime;
import java.util.List;

public interface SysLogService {

    void saveLog(SysLog log);

    PageResult<SysLog> getLogList(String operationType, String module, String username, Integer status, LocalDateTime startTime, LocalDateTime endTime, Integer pageNum, Integer pageSize);

    void clearLogs(Integer days);

    void deleteByIds(List<Long> ids);
}
