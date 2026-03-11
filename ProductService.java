package com.stockmaster.modules.stock.service;

import com.stockmaster.modules.stock.entity.Product;
import com.stockmaster.common.enums.StockStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product updateProduct(Product product);
    void deleteProduct(Long id);
    Product getProductById(Long id);
    Product getProductByCode(String productCode);
    Page<Product> getProducts(String keyword, String category, StockStatus status, Pageable pageable);
    List<Product> getLowStockProducts();
    List<Product> getOverStockProducts();
    void updateProductStatus(Long productId, StockStatus status);
}