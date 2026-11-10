package com.ll.demo03;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByIdInOrderByIdAsc(Collection<Long> ids);

    List<Article> findByTitleContaining(String title);

    List<Article> findByTitleAndBody(String title, String body);
}
