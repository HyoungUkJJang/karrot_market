package com.numble.karrot.product.service;

import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.exception.ProductNotFoundException;
import com.numble.karrot.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product register(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException());
    }

    @Override
    public List<Product> getMyProductList(Long memberId) {
        return productRepository.findMyProducts(memberId);
    }

    @Override
    public List<Product> getMemberProductList(Long memberId) {
        return productRepository.findByMemberId(memberId);
    }

    @Override
    @Transactional
    public Product updateProduct(Long product_id, Product updateForm) {
        return findProduct(product_id)
                .update(updateForm);
    }

}
