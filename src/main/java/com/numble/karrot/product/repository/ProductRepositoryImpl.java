package com.numble.karrot.product.repository;

import com.numble.karrot.member.domain.QMember;
import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.domain.QProduct;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.numble.karrot.member.domain.QMember.*;
import static com.numble.karrot.product.domain.QProduct.*;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private JPQLQueryFactory jpqlQueryFactory;

    @Override
    public List<Product> findMyProducts(Long memberId) {
        return jpqlQueryFactory
                .select(product)
                .from(product)
                .join(product.member, member)
                .where(product.member.id.eq(memberId))
                .fetch();
    }

}
