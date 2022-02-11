package com.numble.karrot.product.repository;

import com.numble.karrot.config.QueryDslConfig;
import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.domain.ProductInit;
import com.numble.karrot.product.domain.ProductStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
@DisplayName("ProductRepository 클래스")
class ProductRepositoryTest {

    private static final String VALID_TITLE = "product1";
    private static final int VALID_PRICE = 1000;
    private static final String VALID_DESCRIPTION = "description1";
    private static final int VALID_REPLY_COUNT = ProductInit.REPLY_COUNT;
    private static final int VALID_HEART_COUNT = ProductInit.HEART_COUNT;
    private static final ProductStatus VALID_PRODUCT_STATUS = ProductStatus.TRADING;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("저장할 상품이 있을 경우에")
        class Context_exist_product {

            @Test
            @DisplayName("저장소에 상품 저장 후 저장된 상품을 리턴합니다.")
            void It_save_return_product() {

                Product saveProduct = productRepository.save(createProduct());
                assertThat(saveProduct.getTitle())
                        .isEqualTo(VALID_TITLE);

            }

        }

    }

    /**
     * 테스트 상품 객체를 리턴합니다.
     * @return
     */
    Product createProduct() {
        return Product.builder()
                .title(VALID_TITLE)
                .description(VALID_DESCRIPTION)
                .price(VALID_PRICE)
                .replyCount(VALID_REPLY_COUNT)
                .heartCount(VALID_HEART_COUNT)
                .productStatus(VALID_PRODUCT_STATUS)
                .build();
    }

}