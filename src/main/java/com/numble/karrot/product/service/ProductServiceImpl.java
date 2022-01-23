package com.numble.karrot.product.service;

import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.domain.ProductStatus;
import com.numble.karrot.product.exception.ProductNotFoundException;
import com.numble.karrot.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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

    @Override
    public List<Product> getMyHeartsProductList(Long memberId) {
        return productRepository.heartMyProducts(memberId);
    }

    @Override
    @Transactional
    public void changedProductStatus(Product product,ProductStatus status) {
        product.changedStatus(status);
    }

    @Override
    @Transactional
    public void addHeartCount(Product product) {
        product.addHeartCount();
    }

    @Override
    @Transactional
    public void deleteHeartCount(Product product) {
        product.deleteHeartCount();
    }

    @Override
    @Transactional
    public void addReplyCount(Product product) {
        product.addReplyCount();
    }

    @Override
    @Transactional
    public void deleteReplyCount(Product product) {
        product.deleteReplyCount();
    }

    @Override
    public List<Product> getMyTradingProductList(Long memberId, Collection<ProductStatus> productStatuses) {
        return productRepository.findByMemberIdAndProductStatusIn(memberId, productStatuses);
    }

    @Override
    public List<Product> getMyCompleteProductList(Long memberId, ProductStatus productStatus) {
        return productRepository.findByMemberIdAndProductStatus(memberId, productStatus);
    }

    @Override
    @Transactional
    public void deleteProduct(Long product_id) {
        productRepository.deleteById(product_id);
    }
}
