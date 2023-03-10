package com.aysimasavas.productservice.service;

import com.aysimasavas.productservice.dto.ProductRequest;
import com.aysimasavas.productservice.dto.ProductResponse;
import com.aysimasavas.productservice.model.Product;
import com.aysimasavas.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product=Product.builder()
            .name(productRequest.getName())
            .description(productRequest.getDescription())
            .price(productRequest.getPrice())
            .categoryId(productRequest.getCategoryId())
            .build();

        productRepository.save(product);
        log.info("product {} is saved",product.getId());
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products=productRepository.findAll();
        return products.stream().map(this:: mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
            .id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .categoryId(product.getCategoryId())
            .build();
    }

    public List<ProductResponse> getProductsByCategory(String categoryId){
        List<Product> products=productRepository.findByCategoryId(categoryId);
        return products.stream().map(this:: mapToProductResponse).toList();

    }
}
