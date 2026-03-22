package com.stockmaster.modules.purchase.service.impl;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.common.exception.BusinessException;
import com.stockmaster.modules.purchase.dto.SupplierDTO;
import com.stockmaster.modules.purchase.dto.SupplierVO;
import com.stockmaster.modules.purchase.entity.Supplier;
import com.stockmaster.modules.purchase.repository.SupplierEvaluationRepository;
import com.stockmaster.modules.purchase.repository.SupplierRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierEvaluationRepository evaluationRepository;

    @Override
    public SupplierVO getById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException("供应商不存在"));
        return convertToVO(supplier);
    }

    @Override
    @Transactional
    public SupplierVO create(SupplierDTO supplierDTO) {
        if (supplierRepository.existsBySupplierCode(supplierDTO.getSupplierCode())) {
            throw new BusinessException("供应商编码已存在");
        }

        Supplier supplier = convertToEntity(supplierDTO);
        supplier = supplierRepository.save(supplier);
        return convertToVO(supplier);
    }

    @Override
    @Transactional
    public SupplierVO update(Long id, SupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException("供应商不存在"));

        if (!supplier.getSupplierCode().equals(supplierDTO.getSupplierCode()) 
                && supplierRepository.existsBySupplierCode(supplierDTO.getSupplierCode())) {
            throw new BusinessException("供应商编码已存在");
        }

        supplier.setSupplierCode(supplierDTO.getSupplierCode());
        supplier.setSupplierName(supplierDTO.getSupplierName());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setContactPhone(supplierDTO.getContactPhone());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setAddress(supplierDTO.getAddress());
        supplier.setBankName(supplierDTO.getBankName());
        supplier.setBankAccount(supplierDTO.getBankAccount());
        supplier.setTaxNumber(supplierDTO.getTaxNumber());
        supplier.setDescription(supplierDTO.getDescription());

        supplier = supplierRepository.save(supplier);
        return convertToVO(supplier);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException("供应商不存在"));
        supplier.setDeleted(true);
        supplierRepository.save(supplier);
    }

    @Override
    public PageResult<SupplierVO> getList(String keyword, Integer status, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Supplier> page = supplierRepository.findByConditions(keyword, status, pageable);

        List<SupplierVO> list = page.getContent().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(list, page.getTotalElements(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new BusinessException("供应商不存在"));
        supplier.setStatus(status);
        supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public void updateRating(Long id) {
        BigDecimal avgScore = evaluationRepository.avgTotalScoreBySupplierId(id);
        if (avgScore != null) {
            Supplier supplier = supplierRepository.findById(id)
                    .orElseThrow(() -> new BusinessException("供应商不存在"));
            supplier.setRating(avgScore.setScale(2, RoundingMode.HALF_UP));
            supplierRepository.save(supplier);
        }
    }

    @Override
    public List<SupplierVO> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAllActive();
        return suppliers.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private SupplierVO convertToVO(Supplier supplier) {
        SupplierVO vo = new SupplierVO();
        vo.setId(supplier.getId());
        vo.setSupplierCode(supplier.getSupplierCode());
        vo.setSupplierName(supplier.getSupplierName());
        vo.setContactPerson(supplier.getContactPerson());
        vo.setContactPhone(supplier.getContactPhone());
        vo.setEmail(supplier.getEmail());
        vo.setAddress(supplier.getAddress());
        vo.setBankName(supplier.getBankName());
        vo.setBankAccount(supplier.getBankAccount());
        vo.setTaxNumber(supplier.getTaxNumber());
        vo.setStatus(supplier.getStatus());
        vo.setRating(supplier.getRating());
        vo.setDescription(supplier.getDescription());
        vo.setCreateTime(supplier.getCreateTime());

        vo.setEvaluationCount(evaluationRepository.countBySupplierId(supplier.getId()).intValue());
        vo.setAvgQualityScore(evaluationRepository.avgQualityScoreBySupplierId(supplier.getId()));
        vo.setAvgDeliveryScore(evaluationRepository.avgDeliveryScoreBySupplierId(supplier.getId()));
        vo.setAvgServiceScore(evaluationRepository.avgServiceScoreBySupplierId(supplier.getId()));
        vo.setAvgPriceScore(evaluationRepository.avgPriceScoreBySupplierId(supplier.getId()));

        return vo;
    }

    private Supplier convertToEntity(SupplierDTO dto) {
        Supplier supplier = new Supplier();
        supplier.setId(dto.getId());
        supplier.setSupplierCode(dto.getSupplierCode());
        supplier.setSupplierName(dto.getSupplierName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setContactPhone(dto.getContactPhone());
        supplier.setEmail(dto.getEmail());
        supplier.setAddress(dto.getAddress());
        supplier.setBankName(dto.getBankName());
        supplier.setBankAccount(dto.getBankAccount());
        supplier.setTaxNumber(dto.getTaxNumber());
        supplier.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        supplier.setRating(dto.getRating());
        supplier.setDescription(dto.getDescription());
        return supplier;
    }
}
