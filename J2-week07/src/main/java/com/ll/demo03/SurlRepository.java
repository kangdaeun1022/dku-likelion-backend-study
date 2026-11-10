package com.ll.demo03;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurlRepository extends JpaRepository<Surl, Long> {
    @Override
    @EntityGraph(attributePaths = "author")
    List<Surl> findAll();
}
