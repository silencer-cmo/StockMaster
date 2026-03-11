package com.stockmaster.modules.stock.service.impl;

import com.stockmaster.modules.stock.entity.Product;
import com.stockmaster.modules.stock.repository.ProductRepository;
import com.stockmaster.modules.stock.service.ProductService;
import com.stockmaster.common.enums.StockStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product getProductByCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Page<Product> getProducts(String keyword, String category, StockStatus status, Pageable pageable) {
        if (keyword != null) {
            return productRepository.findByProductNameContainingOrProductCodeContaining(keyword, keyword, pageable);
        } else if (category != null) {
            return productRepository.findByCategory(category, pageable);
        } else if (status != null) {
            return productRepository.findByStatus(status, pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Override
    public List<Product> getLowStockProducts() {
        return productRepository.findByQuantityLessThanEqual(10);
    }

    @Override
    public List<Product> getOverStockProducts() {
        return productRepository.findByQuantityGreaterThanEqual(1000);
    }

    @Override
    public void updateProductStatus(Long productId, StockStatus status) {
        Product product = getProductById(productId);
        product.setStatus(status);
        productRepository.save(product);
    }
}