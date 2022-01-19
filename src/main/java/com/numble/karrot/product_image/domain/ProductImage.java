package com.numble.karrot.product_image.domain;

import com.numble.karrot.common.BaseEntity;
import com.numble.karrot.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 상품 이미지 정보 도메인 클래스입니다.
 */
@Getter
@Entity
@NoArgsConstructor
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long id;

    private String filePath;
    private String originalFileName;
    private String serverFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    @Builder
    public ProductImage(String filePath, String originalFileName, String serverFileName, Product product) {
        this.filePath = filePath;
        this.originalFileName = originalFileName;
        this.serverFileName = serverFileName;
        this.product = product;
    }

}
