package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Collection<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product create(Product product) {
        product.setId(null);
        return productRepository.save(product);
    }

    public Product update(Long id, Product product) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        product.setId(id);
        return productRepository.save(product);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}
