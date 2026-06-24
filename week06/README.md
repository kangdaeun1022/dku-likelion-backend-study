# Books CRUD API

Spring Boot 3와 Java 21 기반의 책 관리 REST API 프로젝트입니다.

## 기술 스택

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Maven

## 디렉토리 구조

```text
.
├── README.md
├── pom.xml
└── src
    └── main
        ├── java
        │   └── com
        │       └── example
        │           └── booksapi
        │               ├── BooksApiApplication.java
        │               ├── controller
        │               │   └── BookController.java
        │               ├── entity
        │               │   └── Book.java
        │               ├── repository
        │               │   └── BookRepository.java
        │               └── service
        │                   └── BookService.java
        └── resources
            └── application.yml
```

## 실행 방법

Java 21이 설치되어 있어야 합니다.

```bash
mvn spring-boot:run
```

서버 실행 후 기본 주소는 다음과 같습니다.

```text
http://localhost:8080
```

H2 Console은 다음 주소에서 접근할 수 있습니다.

```text
http://localhost:8080/h2-console
```

H2 접속 정보:

```text
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password:
```

## API 목록

| 기능 | Method | URL |
| --- | --- | --- |
| 책 등록 | POST | `/books` |
| 전체 조회 | GET | `/books` |
| 단건 조회 | GET | `/books/{id}` |
| 책 수정 | PUT | `/books/{id}` |
| 책 삭제 | DELETE | `/books/{id}` |

존재하지 않는 id를 조회, 수정, 삭제하면 `404 Not Found`를 반환합니다.

## Postman 테스트 예시

### 1. 책 등록

- Method: `POST`
- URL: `http://localhost:8080/books`
- Headers: `Content-Type: application/json`
- Body:

```json
{
  "name": "Spring Boot",
  "price": 30000,
  "author": "Kim"
}
```

예상 응답:

```json
{
  "id": 1,
  "name": "Spring Boot",
  "price": 30000,
  "author": "Kim"
}
```

### 2. 전체 조회

- Method: `GET`
- URL: `http://localhost:8080/books`

예상 응답:

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

### 3. 단건 조회

- Method: `GET`
- URL: `http://localhost:8080/books/1`

예상 응답:

```json
{
  "id": 1,
  "name": "Spring Boot",
  "price": 30000,
  "author": "Kim"
}
```

### 4. 책 수정

- Method: `PUT`
- URL: `http://localhost:8080/books/1`
- Headers: `Content-Type: application/json`
- Body:

```json
{
  "name": "Spring Boot Advanced",
  "price": 35000,
  "author": "Kim"
}
```

예상 응답:

```json
{
  "id": 1,
  "name": "Spring Boot Advanced",
  "price": 35000,
  "author": "Kim"
}
```

### 5. 책 삭제

- Method: `DELETE`
- URL: `http://localhost:8080/books/1`

예상 응답:

```text
204 No Content
```

## 예외 테스트

존재하지 않는 책을 요청하면 `404 Not Found`가 반환됩니다.

```text
GET http://localhost:8080/books/999
PUT http://localhost:8080/books/999
DELETE http://localhost:8080/books/999
```
