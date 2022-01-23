package com.numble.karrot.product_image.service;

import com.numble.karrot.product_image.domain.ProductImage;
import com.numble.karrot.product_image.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService{

    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional
    public ProductImage save(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    @Override
    @Transactional
    public void deleteProductImage(Long product_id) {
        productImageRepository.deleteByProductId(product_id);
    }

}
