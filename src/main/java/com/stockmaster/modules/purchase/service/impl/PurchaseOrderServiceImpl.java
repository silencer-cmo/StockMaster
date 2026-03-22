package com.stockmaster.modules.purchase.service.impl;

import com.stockmaster.common.dto.PageResult;
import com.stockmaster.common.enums.OrderStatus;
import com.stockmaster.common.exception.BusinessException;
import com.stockmaster.common.security.SecurityUtils;
import com.stockmaster.modules.purchase.dto.PurchaseOrderDTO;
import com.stockmaster.modules.purchase.dto.PurchaseOrderItemDTO;
import com.stockmaster.modules.purchase.dto.PurchaseOrderItemVO;
import com.stockmaster.modules.purchase.dto.PurchaseOrderVO;
import com.stockmaster.modules.purchase.entity.PurchaseOrder;
import com.stockmaster.modules.purchase.entity.PurchaseOrderItem;
import com.stockmaster.modules.purchase.repository.PurchaseOrderItemRepository;
import com.stockmaster.modules.purchase.repository.PurchaseOrderRepository;
import com.stockmaster.modules.purchase.repository.SupplierRepository;
import com.stockmaster.modules.purchase.service.PurchaseOrderService;
import com.stockmaster.modules.stock.entity.Inventory;
import com.stockmaster.modules.stock.entity.Product;
import com.stockmaster.modules.stock.repository.InventoryRepository;
import com.stockmaster.modules.stock.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository orderRepository;
    private final PurchaseOrderItemRepository orderItemRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public PurchaseOrderVO getById(Long id) {
        PurchaseOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("采购订单不存在"));
        return convertToVO(order);
    }

    @Override
    @Transactional
    public PurchaseOrderVO create(PurchaseOrderDTO dto) {
        if (!supplierRepository.existsById(dto.getSupplierId())) {
            throw new BusinessException("供应商不存在");
        }

        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNo(generateOrderNo());
        order.setSupplierId(dto.getSupplierId());
        order.setOrderDate(LocalDateTime.now());
        order.setExpectedDate(dto.getExpectedDate());
        order.setStatus(OrderStatus.DRAFT);
        order.setBuyer(SecurityUtils.getCurrentUsername());
        order.setRemark(dto.getRemark());

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PurchaseOrderItem> items = new ArrayList<>();

        for (PurchaseOrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new BusinessException("商品不存在: " + itemDTO.getProductId()));

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setReceivedQuantity(0);
            item.setUnitPrice(itemDTO.getUnitPrice() != null ? itemDTO.getUnitPrice() : product.getCostPrice());
            item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            item.setRemark(itemDTO.getRemark());
            items.add(item);

            totalAmount = totalAmount.add(item.getTotalPrice());
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        for (PurchaseOrderItem item : items) {
            item.setOrderId(order.getId());
        }
        orderItemRepository.saveAll(items);

        return convertToVO(order);
    }

    @Override
    @Transactional
    public PurchaseOrderVO update(Long id, PurchaseOrderDTO dto) {
        PurchaseOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("采购订单不存在"));

        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessException("只能修改草稿状态的订单");
        }

        order.setSupplierId(dto.getSupplierId());
        order.setExpectedDate(dto.getExpectedDate());
        order.setRemark(dto.getRemark());

        // 删除旧明细
        orderItemRepository.deleteByOrderId(id);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PurchaseOrderItem> items = new ArrayList<>();

        for (PurchaseOrderItemDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new BusinessException("商品不存在: " + itemDTO.getProductId()));

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setOrderId(id);
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setReceivedQuantity(0);
            item.setUnitPrice(itemDTO.getUnitPrice() != null ? itemDTO.getUnitPrice() : product.getCostPrice());
            item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            item.setRemark(itemDTO.getRemark());
            items.add(item);

            totalAmount = totalAmount.add(item.getTotalPrice());
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);
        orderItemRepository.saveAll(items);

        return convertToVO(order);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PurchaseOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("采购订单不存在"));

        if (order.getStatus() != OrderStatus.DRAFT && order.getStatus() != OrderStatus.CANCELLED) {
            throw new BusinessException("只能删除草稿或已取消的订单");
        }

        order.setDeleted(true);
        orderRepository.save(order);
        orderItemRepository.deleteByOrderId(id);
    }

    @Override
    public PageResult<PurchaseOrderVO> getList(String keyword, Long supplierId, OrderStatus status, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<PurchaseOrder> page = orderRepository.findByConditions(keyword, supplierId, status, pageable);

        List<PurchaseOrderVO> list = page.getContent().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(list, page.getTotalElements(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, OrderStatus status) {
        PurchaseOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("采购订单不存在"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void approve(Long id) {
        PurchaseOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("采购订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("只能审核待审核状态的订单");
        }

        order.setStatus(OrderStatus.APPROVED);
        order.setApproveTime(LocalDateTime.now());
        order.setApprover(SecurityUtils.getCurrentUsername());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void reject(Long id) {
        PurchaseOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("采购订单不存在"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("只能拒绝待审核状态的订单");
        }

        order.setStatus(OrderStatus.REJECTED);
        order.setApproveTime(LocalDateTime.now());
        order.setApprover(SecurityUtils.getCurrentUsername());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void receive(Long id, List<Long> itemIds) {
        PurchaseOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("采购订单不存在"));

        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new BusinessException("只能收货已审核的订单");
        }

        List<PurchaseOrderItem> items = orderItemRepository.findByOrderId(id);
        boolean allReceived = true;

        for (PurchaseOrderItem item : items) {
            if (itemIds == null || itemIds.contains(item.getId())) {
                int pendingQty = item.getQuantity() - item.getReceivedQuantity();
                if (pendingQty > 0) {
                    item.setReceivedQuantity(item.getQuantity());
                    orderItemRepository.save(item);

                    // 更新库存
                    Inventory inventory = inventoryRepository.findByProductId(item.getProductId())
                            .orElseGet(() -> {
                                Inventory inv = new Inventory();
                                inv.setProductId(item.getProductId());
                                inv.setQuantity(0);
                                inv.setFrozenQuantity(0);
                                inv.setAvailableQuantity(0);
                                return inv;
                            });

                    inventory.setQuantity(inventory.getQuantity() + pendingQty);
                    inventory.setAvailableQuantity(inventory.getAvailableQuantity() + pendingQty);
                    inventoryRepository.save(inventory);
                }
            }

            if (item.getReceivedQuantity() < item.getQuantity()) {
                allReceived = false;
            }
        }

        if (allReceived) {
            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
        }
    }

    private String generateOrderNo() {
        String prefix = "PO";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long count = orderRepository.count();
        return prefix + dateStr + String.format("%06d", count + 1);
    }

    private PurchaseOrderVO convertToVO(PurchaseOrder order) {
        PurchaseOrderVO vo = new PurchaseOrderVO();
        vo.setId(order.getId());
        vo.setOrderNo(order.getOrderNo());
        vo.setSupplierId(order.getSupplierId());
        vo.setOrderDate(order.getOrderDate());
        vo.setExpectedDate(order.getExpectedDate());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setStatus(order.getStatus() != null ? order.getStatus().name() : OrderStatus.DRAFT.name());
        vo.setBuyer(order.getBuyer());
        vo.setApproveTime(order.getApproveTime());
        vo.setApprover(order.getApprover());
        vo.setRemark(order.getRemark());
        vo.setCreateTime(order.getCreateTime());

        supplierRepository.findById(order.getSupplierId())
                .ifPresent(supplier -> vo.setSupplierName(supplier.getSupplierName()));

        List<PurchaseOrderItem> items = orderItemRepository.findByOrderId(order.getId());
        List<PurchaseOrderItemVO> itemVOs = items.stream()
                .map(this::convertItemToVO)
                .collect(Collectors.toList());
        vo.setItems(itemVOs);

        return vo;
    }

    private PurchaseOrderItemVO convertItemToVO(PurchaseOrderItem item) {
        PurchaseOrderItemVO vo = new PurchaseOrderItemVO();
        vo.setId(item.getId());
        vo.setProductId(item.getProductId());
        vo.setQuantity(item.getQuantity());
        vo.setReceivedQuantity(item.getReceivedQuantity());
        vo.setUnitPrice(item.getUnitPrice());
        vo.setTotalPrice(item.getTotalPrice());
        vo.setRemark(item.getRemark());

        productRepository.findById(item.getProductId())
                .ifPresent(product -> {
                    vo.setProductCode(product.getProductCode());
                    vo.setProductName(product.getProductName());
                });

        return vo;
    }
}
