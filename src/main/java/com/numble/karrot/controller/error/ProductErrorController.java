package com.numble.karrot.controller.error;

import com.numble.karrot.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 상품의 예외를 담당하는 에러 컨트롤러 클래스 입니다.
 */
@ControllerAdvice
public class ProductErrorController {

    /**
     * 없는 상품의 아이디로 조회를 할 경우
     * @return 상품이 없다는 페이지로 이동하게 됩니다.
     */
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String ProductNotFound() {
        return "products/ProductNotFound";
    }

}
