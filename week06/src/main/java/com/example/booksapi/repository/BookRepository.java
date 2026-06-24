package com.example.booksapi.repository;

import com.example.booksapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

// Book 엔티티의 CRUD 쿼리를 제공하는 JPA Repository입니다.
public interface BookRepository extends JpaRepository<Book, Long> {
}
