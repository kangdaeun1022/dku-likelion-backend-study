# Books CRUD API

## 프로젝트 핵심

이 프로젝트는 책 정보를 관리하는 REST API입니다.

핵심 기능은 다음 5가지입니다.

- 책 등록
- 전체 책 조회
- 책 단건 조회
- 책 정보 수정
- 책 삭제

이번 구현에서 가장 중요한 부분은 **Controller, Service, Repository, Entity를 계층별로 분리해서 CRUD 흐름을 만든 것**입니다.

## 코드 흐름 한눈에 보기

```text
Client
  ↓ HTTP 요청
BookController
  ↓ 비즈니스 로직 호출
BookService
  ↓ DB 작업 요청
BookRepository
  ↓ JPA가 Entity를 DB와 매핑
Book Entity
```

요청은 Controller에서 받고, 실제 처리 로직은 Service에서 수행합니다. Repository는 데이터베이스 접근만 담당하고, Entity는 DB에 저장될 책 데이터의 구조를 정의합니다.

## 디렉토리 구조

```text
src/main/java/com/example/booksapi
├── BooksApiApplication.java
├── controller
│   └── BookController.java
├── entity
│   └── Book.java
├── repository
│   └── BookRepository.java
└── service
    └── BookService.java
```

## 1. Book Entity

`Book`은 데이터베이스에 저장되는 책 한 권의 정보를 표현합니다.

```java
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer price;
    private String author;
}
```

### 코드 설명

| 코드 | 설명 |
| --- | --- |
| `@Entity` | 이 클래스가 JPA 엔티티임을 의미합니다. |
| `@Id` | 기본 키 필드를 지정합니다. |
| `@GeneratedValue(strategy = GenerationType.IDENTITY)` | id 값을 DB가 자동 증가 방식으로 생성합니다. |
| `name` | 책 이름을 저장합니다. |
| `price` | 책 가격을 저장합니다. |
| `author` | 저자 이름을 저장합니다. |
| Lombok 어노테이션 | getter, setter, 생성자, builder 코드를 자동 생성합니다. |

즉, `Book` 클래스 하나가 DB의 `book` 테이블 구조가 됩니다.

## 2. BookRepository

`BookRepository`는 데이터베이스에 접근하는 계층입니다.

```java
public interface BookRepository extends JpaRepository<Book, Long> {
}
```

### 코드 설명

`JpaRepository<Book, Long>`을 상속했기 때문에 기본 CRUD 메서드를 직접 만들 필요가 없습니다.

| 메서드 | 역할 |
| --- | --- |
| `save(book)` | 책 등록 또는 수정 |
| `findAll()` | 전체 책 조회 |
| `findById(id)` | id로 책 단건 조회 |
| `delete(book)` | 책 삭제 |

Repository에는 별도 코드를 작성하지 않았지만, Spring Data JPA가 런타임에 구현체를 자동으로 만들어줍니다.

## 3. BookService

`BookService`는 CRUD의 실제 처리 흐름을 담당합니다.

### 책 등록

```java
@Transactional
public Book createBook(Book book) {
    return bookRepository.save(book);
}
```

Controller에서 받은 `Book` 객체를 Repository의 `save()` 메서드로 저장합니다.

### 전체 조회

```java
public List<Book> getAllBooks() {
    return bookRepository.findAll();
}
```

DB에 저장된 모든 책을 리스트로 반환합니다.

### 단건 조회

```java
public Book getBookById(Long id) {
    return findBook(id);
}
```

단건 조회는 바로 Repository를 호출하지 않고 `findBook(id)` 공통 메서드를 사용합니다.

### 수정

```java
@Transactional
public Book updateBook(Long id, Book request) {
    Book book = findBook(id);
    book.setName(request.getName());
    book.setPrice(request.getPrice());
    book.setAuthor(request.getAuthor());
    return book;
}
```

수정 로직의 흐름은 다음과 같습니다.

1. id로 기존 책을 찾습니다.
2. 존재하면 name, price, author 값을 변경합니다.
3. `@Transactional` 안에서 엔티티 값이 변경되므로 JPA가 변경 내용을 DB에 반영합니다.

여기서 `bookRepository.save(book)`을 다시 호출하지 않아도 되는 이유는, 조회한 `book` 객체가 영속 상태이기 때문입니다.

### 삭제

```java
@Transactional
public void deleteBook(Long id) {
    Book book = findBook(id);
    bookRepository.delete(book);
}
```

삭제도 먼저 id로 책을 찾고, 존재하는 경우에만 삭제합니다.

### 404 예외 처리

```java
private Book findBook(Long id) {
    return bookRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Book not found. id=" + id
            ));
}
```

`findBook(id)`는 조회, 수정, 삭제에서 공통으로 사용됩니다.

책이 존재하지 않으면 `ResponseStatusException`을 발생시키고, HTTP 상태 코드는 `404 Not Found`로 반환됩니다.

이렇게 공통 메서드로 분리하면 조회, 수정, 삭제마다 예외 처리 코드를 반복하지 않아도 됩니다.

## 4. BookController

`BookController`는 HTTP 요청을 받는 계층입니다.

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
}
```

### 코드 설명

| 코드 | 설명 |
| --- | --- |
| `@RestController` | JSON 응답을 반환하는 REST Controller입니다. |
| `@RequestMapping("/books")` | 모든 API의 기본 경로를 `/books`로 지정합니다. |
| `@RequiredArgsConstructor` | `final` 필드인 `BookService`를 생성자 주입합니다. |

## API별 Controller 코드 설명

### 1. 책 등록

```java
@PostMapping
public ResponseEntity<Book> createBook(@RequestBody Book request) {
    Book response = bookService.createBook(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
```

- `POST /books` 요청을 처리합니다.
- `@RequestBody`로 JSON 요청 값을 `Book` 객체로 받습니다.
- 저장 후 생성된 `id`를 포함한 책 정보를 응답합니다.
- 등록 성공이므로 상태 코드는 `201 Created`를 반환합니다.

### 2. 전체 조회

```java
@GetMapping
public ResponseEntity<List<Book>> getAllBooks() {
    return ResponseEntity.ok(bookService.getAllBooks());
}
```

- `GET /books` 요청을 처리합니다.
- Service에서 전체 책 목록을 받아 배열 형태로 반환합니다.
- 성공 상태 코드는 `200 OK`입니다.

### 3. 단건 조회

```java
@GetMapping("/{id}")
public ResponseEntity<Book> getBook(@PathVariable Long id) {
    return ResponseEntity.ok(bookService.getBookById(id));
}
```

- `GET /books/{id}` 요청을 처리합니다.
- `@PathVariable`로 URL의 id 값을 받습니다.
- 해당 id의 책이 있으면 `200 OK`, 없으면 Service에서 `404 Not Found`를 반환합니다.

### 4. 책 수정

```java
@PutMapping("/{id}")
public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book request) {
    return ResponseEntity.ok(bookService.updateBook(id, request));
}
```

- `PUT /books/{id}` 요청을 처리합니다.
- URL의 id로 수정할 책을 찾습니다.
- Request Body의 값으로 기존 책 정보를 변경합니다.
- 수정된 책 정보를 응답합니다.

### 5. 책 삭제

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    bookService.deleteBook(id);
    return ResponseEntity.noContent().build();
}
```

- `DELETE /books/{id}` 요청을 처리합니다.
- 삭제 성공 시 응답 본문 없이 `204 No Content`를 반환합니다.
- 삭제할 id가 없으면 Service에서 `404 Not Found`를 반환합니다.

## API 요청 예시

### 책 등록

```http
POST /books
Content-Type: application/json
```

```json
{
  "name": "Spring Boot",
  "price": 30000,
  "author": "Kim"
}
```

응답:

```json
{
  "id": 1,
  "name": "Spring Boot",
  "price": 30000,
  "author": "Kim"
}
```

### 전체 조회

```http
GET /books
```

```json
[
  {
    "id": 1,
    "name": "Spring Boot",
    "price": 30000,
    "author": "Kim"
  }
]
```

### 단건 조회

```http
GET /books/1
```

### 수정

```http
PUT /books/1
Content-Type: application/json
```

```json
{
  "name": "Spring Boot Advanced",
  "price": 35000,
  "author": "Kim"
}
```

### 삭제

```http
DELETE /books/1
```

응답 상태:

```text
204 No Content
```

## 발표할 때 강조할 부분

1. `BookController`는 HTTP 요청과 응답만 담당합니다.
2. `BookService`는 CRUD 로직과 예외 처리를 담당합니다.
3. `BookRepository`는 JPA를 통해 DB 접근을 담당합니다.
4. `Book` Entity는 DB에 저장될 데이터 구조를 정의합니다.
5. 존재하지 않는 id는 Service의 `findBook()`에서 공통으로 처리해 `404 Not Found`를 반환합니다.

## 정리

이 프로젝트의 핵심은 단순히 CRUD API를 만든 것이 아니라, Spring Boot에서 많이 사용하는 계층형 구조로 책임을 나누어 구현했다는 점입니다.

Controller는 요청을 받고, Service는 로직을 처리하고, Repository는 DB와 연결하고, Entity는 데이터를 표현합니다. 이 구조 덕분에 코드 역할이 명확해지고 유지보수가 쉬워집니다.
