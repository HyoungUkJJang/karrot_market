package com.numble.karrot.config;

import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * QueryDSL 설정 클래스 입니다.
 */
@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPQLQueryFactory jpqlQueryFactory() {
        return new JPAQueryFactory(em);
    }

}
