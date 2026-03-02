package com.inventory.agent.service;

import com.inventory.agent.model.Product;
import com.inventory.agent.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Add new product
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Update product stock
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setName(productDetails.getName());
        product.setQuantity(productDetails.getQuantity());
        product.setThreshold(productDetails.getThreshold());
        product.setPrice(productDetails.getPrice());

        return productRepository.save(product);
    }

    // Get low stock products (SMART INVENTORY LOGIC)
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    // Delete product
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
