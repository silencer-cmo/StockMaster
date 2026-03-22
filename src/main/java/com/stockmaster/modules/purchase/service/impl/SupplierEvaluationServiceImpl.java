package com.stockmaster.modules.purchase.service.impl;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.common.exception.BusinessException;
import com.stockmaster.modules.purchase.dto.SupplierEvaluationDTO;
import com.stockmaster.modules.purchase.entity.SupplierEvaluation;
import com.stockmaster.modules.purchase.repository.SupplierEvaluationRepository;
import com.stockmaster.modules.purchase.repository.SupplierRepository;
import com.stockmaster.modules.purchase.service.SupplierEvaluationService;
import com.stockmaster.modules.purchase.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class SupplierEvaluationServiceImpl implements SupplierEvaluationService {

    private final SupplierEvaluationRepository evaluationRepository;
    private final SupplierRepository supplierRepository;
    private final SupplierService supplierService;

    @Override
    public SupplierEvaluation getById(Long id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("评价记录不存在"));
    }

    @Override
    @Transactional
    public SupplierEvaluation create(SupplierEvaluationDTO dto) {
        if (!supplierRepository.existsById(dto.getSupplierId())) {
            throw new BusinessException("供应商不存在");
        }

        SupplierEvaluation evaluation = new SupplierEvaluation();
        evaluation.setSupplierId(dto.getSupplierId());
        evaluation.setOrderId(dto.getOrderId());
        evaluation.setQualityScore(dto.getQualityScore());
        evaluation.setDeliveryScore(dto.getDeliveryScore());
        evaluation.setServiceScore(dto.getServiceScore());
        evaluation.setPriceScore(dto.getPriceScore());
        evaluation.setContent(dto.getContent());

        // 计算总分
        BigDecimal totalScore = calculateTotalScore(dto);
        evaluation.setTotalScore(totalScore);

        evaluation = evaluationRepository.save(evaluation);

        // 更新供应商评分
        supplierService.updateRating(dto.getSupplierId());

        return evaluation;
    }

    @Override
    @Transactional
    public SupplierEvaluation update(Long id, SupplierEvaluationDTO dto) {
        SupplierEvaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("评价记录不存在"));

        evaluation.setQualityScore(dto.getQualityScore());
        evaluation.setDeliveryScore(dto.getDeliveryScore());
        evaluation.setServiceScore(dto.getServiceScore());
        evaluation.setPriceScore(dto.getPriceScore());
        evaluation.setContent(dto.getContent());

        // 重新计算总分
        BigDecimal totalScore = calculateTotalScore(dto);
        evaluation.setTotalScore(totalScore);

        evaluation = evaluationRepository.save(evaluation);

        // 更新供应商评分
        supplierService.updateRating(evaluation.getSupplierId());

        return evaluation;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        SupplierEvaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("评价记录不存在"));
        
        Long supplierId = evaluation.getSupplierId();
        evaluation.setDeleted(true);
        evaluationRepository.save(evaluation);

        // 更新供应商评分
        supplierService.updateRating(supplierId);
    }

    @Override
    public PageResult<SupplierEvaluation> getList(Long supplierId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<SupplierEvaluation> page = evaluationRepository.findBySupplierId(supplierId, pageable);
        return PageResult.of(page.getContent(), page.getTotalElements(), pageNum, pageSize);
    }

    private BigDecimal calculateTotalScore(SupplierEvaluationDTO dto) {
        int count = 0;
        BigDecimal sum = BigDecimal.ZERO;

        if (dto.getQualityScore() != null) {
            sum = sum.add(BigDecimal.valueOf(dto.getQualityScore()));
            count++;
        }
        if (dto.getDeliveryScore() != null) {
            sum = sum.add(BigDecimal.valueOf(dto.getDeliveryScore()));
            count++;
        }
        if (dto.getServiceScore() != null) {
            sum = sum.add(BigDecimal.valueOf(dto.getServiceScore()));
            count++;
        }
        if (dto.getPriceScore() != null) {
            sum = sum.add(BigDecimal.valueOf(dto.getPriceScore()));
            count++;
        }

        if (count > 0) {
            return sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
