package com.numble.karrot.product.service;

import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.domain.ProductInit;
import com.numble.karrot.product.domain.ProductStatus;
import com.numble.karrot.product.exception.ProductNotFoundException;
import com.numble.karrot.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/*
register [v]
getAllProducts [v]
findProduct [v]
getMyProductList [v]
getMemberProductList [v]
updateProduct [v]
getMyHeartsProductList [v]
getMyTradingProductList [v]
getMyCompleteProductList [v]

deleteProduct
changedProductStatus
addHeartCount
deleteHeartCount
addReplyCount
deleteReplyCount
 */
@DisplayName("ProductService 클래스")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    // 너무 많은 정보를 선언한게 아닐까
    private static final Long VALID_MEMBERID = 1L;
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 9999L;

    private static final int VALID_PRICE = 1000;
    private static final int VALID_REPLY_COUNT = ProductInit.REPLY_COUNT;
    private static final int VALID_HEART_COUNT = ProductInit.HEART_COUNT;
    private static final int VALID_PRODUCTS_SIZE = createProducts().size();

    private static final String VALID_DESCRIPTION = "description1";
    private static final String VALID_TITLE = "product1";

    private static final ProductStatus VALID_PRODUCT_STATUS = ProductStatus.TRADING;
    private static final List<ProductStatus> VALID_PRODUCT_STATUSES = Arrays.asList(ProductStatus.TRADING, ProductStatus.RESERVED);

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @Nested
    @DisplayName("register 메서드는")
    class Describe_register {

        @Nested
        @DisplayName("상품등록 요청이 들어온 경우에")
        class Context_req_product{

            Product product;

            @BeforeEach
            void setUp() {
                product = createProduct();
                given(productRepository.save(any(Product.class)))
                        .willReturn(product);
            }

            @Test
            @DisplayName("저장소에 상품을 저장하고 저장 된 상품을 리턴한다.")
            void It_saved_return_product() {
                Product registerProduct = productService.register(createProduct());
                assertThat(registerProduct.getTitle()).isEqualTo(VALID_TITLE);
                verify(productRepository).save(any(Product.class));
            }

        }

    }

    @Nested
    @DisplayName("getAllProducts 메서드는")
    class Describe_getAllProducts{

        @Nested
        @DisplayName("상품목록 조회 요청이 들어온 경우에")
        class Context_exist_products {

            List<Product> products;

            @BeforeEach
            void setUp() {
                products = createProducts();
                given(productRepository.findAll())
                        .willReturn(products);
            }

            @Test
            @DisplayName("저장소에서 상품 리스트를 리턴합니다.")
            void It_return_products() {
                List<Product> products = productService.getAllProducts();
                assertThat(products).hasSize(VALID_PRODUCTS_SIZE);
                verify(productRepository).findAll();
            }

        }

    }

    @Nested
    @DisplayName("findProduct 메서드는")
    class Describe_findProduct {

        @Nested
        @DisplayName("상세조회 할 상품이 존재할 경우에")
        class Context_exist_product {

            Product product;

            @BeforeEach
            void setUp() {
                product = createProduct();
                given(productRepository.findById(any(Long.class)))
                        .willReturn(Optional.of(product));
            }

            @Test
            @DisplayName("상품의 아이디를 통해 조회하여 조회된 상품을 리턴합니다.")
            void It_select_productId_return() {
                Product findProduct = productService.findProduct(VALID_ID);
                assertThat(findProduct.getTitle()).isEqualTo(VALID_TITLE);
                assertThat(findProduct.getPrice()).isEqualTo(VALID_PRICE);
                verify(productRepository).findById(any(Long.class));
            }

        }

        @Nested
        @DisplayName("상세조회 할 상품이 존재하지 않을 경우에")
        class Context_not_exist_product {

            Product product;

            @BeforeEach
            void setUp() {
                product = createProduct();
                given(productRepository.findById(any(Long.class)))
                        .willThrow(ProductNotFoundException.class);
            }

            @Test
            @DisplayName("ProductNotFoundException 에외를 던진다.")
            void It_return_productNotFoundException() {
                assertThatThrownBy(() -> {
                    productService.findProduct(INVALID_ID);
                }).isInstanceOf(ProductNotFoundException.class);
                verify(productRepository).findById(any(Long.class));
            }

        }

    }

    @Nested
    @DisplayName("getMemberProductList 메서드는")
    class Describe_getMemberProductList{

        @Nested
        @DisplayName("특정 회원의 상품 리스트를 조회하고 싶을 경우에")
        class Context_member_products {

            List<Product> products;

            @BeforeEach
            void setUp() {
                products = createProducts();
                given(productRepository.findByMemberId(any(Long.class)))
                        .willReturn(products);
            }

            @Test
            @DisplayName("회원의 아이디를 통해 상품리스트를 조회하여 리턴합니다.")
            void It_select_memberId_products() {
                List<Product> memberProducts = productService.getMemberProductList(VALID_MEMBERID);
                assertThat(memberProducts).hasSize(10);
            }

        }

    }

    @Nested
    @DisplayName("getMyProductList 메서드는")
    class Describe_getMyProductList {

        @Nested
        @DisplayName("자신의 상품 리스트를 조회하고 싶을 경우에")
        class Context_member_products {

            List<Product> products;

            @BeforeEach
            void setUp() {
                products = createProducts();
                given(productRepository.findMyProducts(any(Long.class)))
                        .willReturn(products);
            }

            @Test
            @DisplayName("자신의 아이디를 통해 상품리스트를 조회하여 리턴합니다.")
            void It_select_memberId_products() {
                List<Product> memberProducts = productService.getMyProductList(VALID_MEMBERID);
                assertThat(memberProducts).hasSize(10);
            }

        }

    }

    @Nested
    @DisplayName("getMyHeartsProductList 메서드는")
    class Describe_getMyHeartsProductList {

        @Nested
        @DisplayName("자신이 관심상품으로 등록한 상품 리스트를 조회하고 싶을 경우에")
        class Context_select_myHeart_products {

            List<Product> products;

            @BeforeEach
            void setUp() {
                products = createProducts();
                given(productRepository.heartMyProducts(any(Long.class)))
                        .willReturn(products);
            }

            @Test
            @DisplayName("회원의 아이디를 통해 관심상품 목록을 조회하여 리턴합니다.")
            void It_return_myHeartProducts() {
                List<Product> myHeartsProducts = productService.getMyHeartsProductList(VALID_ID);
                assertThat(myHeartsProducts).hasSize(VALID_PRODUCTS_SIZE);
            }

        }

    }

    @Nested
    @DisplayName("getMyTradingProductList 메서드는")
    class Describe_getMyTradingProductList {

        @Nested
        @DisplayName("자신이 현재 거래중인 상품목록을 조회하고 싶을 경우에")
        class Context_select_myHeart_products {

            List<Product> products;

            @BeforeEach
            void setUp() {
                products = createProducts();
                given(productRepository.findByMemberIdAndProductStatusIn(any(Long.class),
                        any(List.class)))
                        .willReturn(products);
            }

            @Test
            @DisplayName("회원의 아이디, 거래상태를 통해 거래중인 상품 목록을 조회하여 리턴합니다.")
            void It_return_myHeartProducts() {
                List<Product> myTradingProducts = productService.getMyTradingProductList(VALID_ID, VALID_PRODUCT_STATUSES);
                assertThat(myTradingProducts).hasSize(VALID_PRODUCTS_SIZE);
            }

        }

    }

    @Nested
    @DisplayName("getMyCompleteProductList 메서드는")
    class Describe_getMyCompleteProductList {

        @Nested
        @DisplayName("자신이 현재 거래완료된 상품목록을 조회하고 싶을 경우에")
        class Context_select_myHeart_products {

            List<Product> products;

            @BeforeEach
            void setUp() {
                products = createProducts();
                given(productRepository.findByMemberIdAndProductStatus(any(Long.class),
                        any(ProductStatus.class)))
                        .willReturn(products);
            }

            @Test
            @DisplayName("회원의 아이디, 거래상태를 통해 거래 완료된 상품 목록을 조회하여 리턴합니다.")
            void It_return_myHeartProducts() {
                List<Product> myTradingProducts = productService.getMyCompleteProductList(VALID_ID, ProductStatus.TRADE_COMPLETED);
                assertThat(myTradingProducts).hasSize(VALID_PRODUCTS_SIZE);
            }

        }

    }

    @Nested
    @DisplayName("updateProduct 메서드는")
    class Describe_updateProduct {

        @Nested
        @DisplayName("수정할 상품의 내용이 있을 경우에")
        class Context_edit_product {

            @BeforeEach
            void setUp() {
                given(productRepository.findById(any(Long.class)))
                        .willReturn(Optional.of(updateProduct()));
            }

            @Test
            @DisplayName("상품의 아이디, 수정 폼을 통해 상품 수정 후 리턴합니다.")
            void It_return_updatedProduct() {
                Product updateProduct = productService.updateProduct(VALID_ID, updateProduct());
                assertThat(updateProduct.getTitle()).isEqualTo(VALID_TITLE + "update");
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

    /**
     * 테스트 수정 객체를 리턴합니다.
     * @return
     */
    Product updateProduct() {
        return Product.builder()
                .title(VALID_TITLE + "update")
                .description(VALID_DESCRIPTION)
                .price(VALID_PRICE)
                .replyCount(VALID_REPLY_COUNT)
                .heartCount(VALID_HEART_COUNT)
                .productStatus(VALID_PRODUCT_STATUS)
                .build();
    }

    /**
     * 테스트 상품 리스트를 리턴합니다.
     * @return
     */
    static List<Product> createProducts() {

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            products.add(Product.builder()
                    .title(VALID_TITLE + i)
                    .description(VALID_DESCRIPTION + i)
                    .price(VALID_PRICE + i)
                    .heartCount(VALID_HEART_COUNT + i)
                    .replyCount(VALID_REPLY_COUNT + i)
                    .productStatus(VALID_PRODUCT_STATUS)
                    .build());
        }
        return products;
    }

}
