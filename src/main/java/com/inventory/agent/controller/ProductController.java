package com.inventory.agent.controller;

import com.inventory.agent.model.Product;
import com.inventory.agent.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // GET /products - Retrieve the full list of inventory
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // GET /products/{id} - Get single product
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /products - Add new inventory items
    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product newProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    // PUT /products/{id} - Update stock levels (e.g., when a sale happens)
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /products/{id} - Remove product
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // GET /alerts/low-stock - Special endpoint for immediate problems
    @GetMapping("/alerts/low-stock")
    public ResponseEntity<List<Product>> getLowStockAlerts() {
        List<Product> lowStockProducts = productService.getLowStockProducts();
        return ResponseEntity.ok(lowStockProducts);
    }
}
