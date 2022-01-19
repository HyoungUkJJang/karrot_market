package com.numble.karrot.product.dto;

import com.numble.karrot.category.domain.Category;
import com.numble.karrot.member.domain.Member;
import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.domain.ProductInit;
import com.numble.karrot.product.domain.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품등록 요청 폼 DTO 클래스 입니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductRegisterRequest {

    @NotEmpty(message = "상품 이름은 필수입니다.")
    private String title;
    @NotEmpty(message = "가격 입력은 필수입니다.")
    private int price;
    @NotEmpty(message = "상품 설명은 필수입니다.")
    private String description;
    private Category category;
    List<MultipartFile> productImages;

    @Builder
    public ProductRegisterRequest(String title, Category category, int price, String description, ArrayList<MultipartFile> productImages) {
        this.title = title;
        this.category = category;
        this.price = price;
        this.description = description;
        this.productImages = productImages;
    }

    /**
     * 상품 엔티티로 변환합니다.
     * @param member 상품과 대응하는 유저 정보
     * @return 저장 할 상품 엔티티
     */
    public Product toProductEntity(Member member) {
        return Product.builder()
                .title(this.getTitle())
                .description(this.getDescription())
                .price(this.getPrice())
                .category(this.getCategory())
                .heartCount(ProductInit.HEART_COUNT)
                .replyCount(ProductInit.REPLY_COUNT)
                .productStatus(ProductStatus.TRADING)
                .member(member)
                .build();
    }

}
