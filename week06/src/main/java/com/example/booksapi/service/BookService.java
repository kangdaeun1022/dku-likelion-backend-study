package com.example.booksapi.service;

import com.example.booksapi.entity.Book;
import com.example.booksapi.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

// Book 관련 비즈니스 로직을 담당하는 서비스 계층입니다.
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return findBook(id);
    }

    @Transactional
    public Book updateBook(Long id, Book request) {
        Book book = findBook(id);
        book.setName(request.getName());
        book.setPrice(request.getPrice());
        book.setAuthor(request.getAuthor());
        return book;
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findBook(id);
        bookRepository.delete(book);
    }

    // 존재하지 않는 id 요청은 REST API 요구사항에 맞게 404로 변환합니다.
    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Book not found. id=" + id
                ));
    }
}
