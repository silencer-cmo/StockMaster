package com.stockmaster.modules.system.service.impl;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.modules.system.entity.SysLog;
import com.stockmaster.modules.system.repository.SysLogRepository;
import com.stockmaster.modules.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysLogServiceImpl implements SysLogService {

    private final SysLogRepository sysLogRepository;

    @Override
    public void saveLog(SysLog log) {
        sysLogRepository.save(log);
    }

    @Override
    public PageResult<SysLog> getLogList(String operationType, String module, String username, 
                                         Integer status, LocalDateTime startTime, LocalDateTime endTime,
                                         Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<SysLog> page = sysLogRepository.findByConditions(operationType, module, username, status, startTime, endTime, pageable);
        return PageResult.of(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void clearLogs(Integer days) {
        LocalDateTime beforeTime = LocalDateTime.now().minusDays(days);
        sysLogRepository.deleteByCreateTimeBefore(beforeTime);
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        sysLogRepository.deleteAllById(ids);
    }
}
