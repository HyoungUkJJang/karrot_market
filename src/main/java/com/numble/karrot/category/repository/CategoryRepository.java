package com.numble.karrot.category.repository;

import com.numble.karrot.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 카테고리 리포지토리 입니다.
 */
public interface CategoryRepository extends JpaRepository<Category, String> {
}
