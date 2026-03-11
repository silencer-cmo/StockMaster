package com.stockmaster.modules.stock.controller;

import com.stockmaster.modules.stock.entity.Product;
import com.stockmaster.modules.stock.service.ProductService;
import com.stockmaster.common.enums.StockStatus;
import com.stockmaster.common.aop.LogOperation;
import com.stockmaster.common.enums.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/stock/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @LogOperation(OperationType.CREATE)
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @LogOperation(OperationType.UPDATE)
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @LogOperation(OperationType.DELETE)
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @LogOperation(OperationType.QUERY)
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/code/{productCode}")
    @LogOperation(OperationType.QUERY)
    public ResponseEntity<Product> getProductByCode(@PathVariable String productCode) {
        Product product = productService.getProductByCode(productCode);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    @LogOperation(OperationType.QUERY)
    public ResponseEntity<Page<Product>> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) StockStatus status,
            Pageable pageable) {
        Page<Product> products = productService.getProducts(keyword, category, status, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/low-stock")
    @LogOperation(OperationType.QUERY)
    public ResponseEntity<List<Product>> getLowStockProducts() {
        List<Product> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/over-stock")
    @LogOperation(OperationType.QUERY)
    public ResponseEntity<List<Product>> getOverStockProducts() {
        List<Product> products = productService.getOverStockProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}/status")
    @LogOperation(OperationType.UPDATE)
    public ResponseEntity<Void> updateProductStatus(@PathVariable Long id, @RequestParam StockStatus status) {
        productService.updateProductStatus(id, status);
        return ResponseEntity.ok().build();
    }
}