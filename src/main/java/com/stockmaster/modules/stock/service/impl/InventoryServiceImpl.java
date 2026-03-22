package com.stockmaster.modules.stock.service.impl;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.common.exception.BusinessException;
import com.stockmaster.modules.stock.dto.InventoryDTO;
import com.stockmaster.modules.stock.entity.Inventory;
import com.stockmaster.modules.stock.entity.Product;
import com.stockmaster.modules.stock.repository.InventoryRepository;
import com.stockmaster.modules.stock.repository.ProductRepository;
import com.stockmaster.modules.stock.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public InventoryDTO getById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("库存记录不存在"));
        return convertToDTO(inventory);
    }

    @Override
    public InventoryDTO getByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new BusinessException("库存记录不存在"));
        return convertToDTO(inventory);
    }

    @Override
    public PageResult<InventoryDTO> getList(String keyword, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Inventory> page = inventoryRepository.findByKeyword(keyword, pageable);

        List<InventoryDTO> list = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return PageResult.of(list, page.getTotalElements(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void updateQuantity(Long id, Integer quantity) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("库存记录不存在"));
        inventory.setQuantity(quantity);
        inventory.setAvailableQuantity(quantity - inventory.getFrozenQuantity());
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public void updateWarning(Long id, Integer warningMin, Integer warningMax) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException("库存记录不存在"));
        inventory.setWarningMin(warningMin);
        inventory.setWarningMax(warningMax);
        inventoryRepository.save(inventory);
    }

    @Override
    public List<InventoryDTO> getLowStockList() {
        List<Inventory> inventories = inventoryRepository.findLowStock();
        return inventories.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<InventoryDTO> getOverStockList() {
        List<Inventory> inventories = inventoryRepository.findOverStock();
        return inventories.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Long getTotalQuantity() {
        Long total = inventoryRepository.getTotalStockQuantity();
        return total != null ? total : 0L;
    }

    @Override
    public Long getLowStockCount() {
        Long count = inventoryRepository.countLowStockProducts();
        return count != null ? count : 0L;
    }

    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setProductId(inventory.getProductId());
        dto.setWarehouseCode(inventory.getWarehouseCode());
        dto.setQuantity(inventory.getQuantity());
        dto.setFrozenQuantity(inventory.getFrozenQuantity());
        dto.setAvailableQuantity(inventory.getAvailableQuantity());
        dto.setBatchNo(inventory.getBatchNo());
        dto.setShelfLocation(inventory.getShelfLocation());
        dto.setWarningMin(inventory.getWarningMin());
        dto.setWarningMax(inventory.getWarningMax());

        productRepository.findById(inventory.getProductId())
                .ifPresent(product -> {
                    dto.setProductCode(product.getProductCode());
                    dto.setProductName(product.getProductName());
                    dto.setUnitPrice(product.getCostPrice());
                });

        return dto;
    }
}
