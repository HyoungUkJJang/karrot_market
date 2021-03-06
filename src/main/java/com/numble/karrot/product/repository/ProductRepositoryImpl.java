package com.numble.karrot.product.repository;

import com.numble.karrot.heart.domain.QHeart;
import com.numble.karrot.member.domain.QMember;
import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.domain.QProduct;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

import static com.numble.karrot.heart.domain.QHeart.heart;
import static com.numble.karrot.member.domain.QMember.*;
import static com.numble.karrot.product.domain.QProduct.*;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final EntityManager em;
    private final JPQLQueryFactory jpqlQueryFactory;

    @Override
    public List<Product> findMyProducts(Long memberId) {
        return jpqlQueryFactory
                .select(product)
                .from(product)
                .join(product.member, member)
                .where(product.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<Product> heartMyProducts(Long memberId) {
       return jpqlQueryFactory
                .selectFrom(product)
                .where(product.id.in(
                        JPAExpressions
                                .select(heart.product.id)
                                .from(heart)
                                .where(heart.member.id.eq(memberId))
                ))
                .fetch();
    }

}
