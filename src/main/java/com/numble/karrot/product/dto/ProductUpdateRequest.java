package com.numble.karrot.product.dto;

import com.numble.karrot.category.domain.Category;
import com.numble.karrot.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품 수정하는 DTO 클래스 입니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateRequest {

    @NotEmpty(message = "상품 이름은 필수입니다.")
    private String title;
    @NotNull(message = "가격 입력은 필수입니다.")
    private int price;
    @NotEmpty(message = "상품 설명은 필수입니다.")
    private String description;
    private Category category;
    List<MultipartFile> productImages;

    @Builder
    public ProductUpdateRequest(String title, Category category, int price, String description, ArrayList<MultipartFile> productImages) {
        this.title = title;
        this.category = category;
        this.price = price;
        this.description = description;
        this.productImages = productImages;
    }

    public Product toProductEntity() {
        return Product.builder()
                .title(this.title)
                .price(this.price)
                .category(this.category)
                .description(this.description)
                .build();
    }

}
