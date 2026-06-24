# Books CRUD API

## 프로젝트 소개

이 프로젝트는 **Spring Boot 3와 Java 21**을 사용해서 만든 책 관리 REST API입니다.

발표 주제는 간단합니다.

> 책 정보를 등록하고, 조회하고, 수정하고, 삭제하는 CRUD API를 계층 구조로 구현한다.

사용자는 책의 이름, 가격, 저자를 입력할 수 있고 서버는 H2 인메모리 데이터베이스에 데이터를 저장합니다.

## 구현 목표

이번 프로젝트에서 중점적으로 구현한 내용은 다음과 같습니다.

- REST API 기본 CRUD 흐름 이해
- Controller, Service, Repository, Entity 계층 분리
- Spring Data JPA를 사용한 데이터베이스 접근
- H2 Database를 사용한 빠른 로컬 테스트 환경 구성
- 존재하지 않는 데이터 요청 시 `404 Not Found` 반환
- Postman으로 API 요청과 응답 확인

## 기술 스택

| 구분 | 사용 기술 |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Web | Spring Web |
| ORM | Spring Data JPA |
| Database | H2 Database |
| Library | Lombok |
| Build Tool | Maven |

## 전체 구조

이 프로젝트는 역할별로 패키지를 분리했습니다.

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

## 계층별 역할

### 1. Entity

`Book` 클래스는 데이터베이스 테이블과 매핑되는 객체입니다.

책 데이터는 다음 필드를 가집니다.

| 필드 | 타입 | 설명 |
| --- | --- | --- |
| id | Long | 기본 키, 자동 증가 |
| name | String | 책 이름 |
| price | Integer | 책 가격 |
| author | String | 저자 |

### 2. Repository

`BookRepository`는 `JpaRepository`를 상속받아 기본 CRUD 기능을 제공합니다.

직접 SQL을 작성하지 않아도 `save`, `findAll`, `findById`, `delete` 같은 메서드를 사용할 수 있습니다.

### 3. Service

`BookService`는 실제 비즈니스 로직을 담당합니다.

특히 단건 조회, 수정, 삭제에서는 먼저 id로 책을 찾고, 존재하지 않으면 `404 Not Found`를 반환하도록 처리했습니다.

### 4. Controller

`BookController`는 클라이언트의 HTTP 요청을 받아 Service로 전달하고, 결과를 JSON 응답으로 반환합니다.

API의 기본 경로는 다음과 같습니다.

```text
/books
```

## API 기능

| 기능 | Method | URL | 설명 |
| --- | --- | --- | --- |
| 책 등록 | POST | `/books` | 새 책을 등록합니다. |
| 전체 조회 | GET | `/books` | 등록된 모든 책을 조회합니다. |
| 단건 조회 | GET | `/books/{id}` | id에 해당하는 책 하나를 조회합니다. |
| 책 수정 | PUT | `/books/{id}` | id에 해당하는 책 정보를 수정합니다. |
| 책 삭제 | DELETE | `/books/{id}` | id에 해당하는 책을 삭제합니다. |

## 실행 방법

Java 21이 설치된 환경에서 실행합니다.

```bash
mvn spring-boot:run
```

서버가 실행되면 기본 주소는 다음과 같습니다.

```text
http://localhost:8080
```

## H2 Database 확인

H2 Console은 브라우저에서 다음 주소로 접속합니다.

```text
http://localhost:8080/h2-console
```

접속 정보는 다음과 같습니다.

```text
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password:
```

## application.yml 설정

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb

  h2:
    console:
      enabled: true

  jpa:
    hibernate:
      ddl-auto: create
```

이 설정을 통해 애플리케이션을 실행할 때마다 H2 인메모리 데이터베이스가 생성되고, JPA가 Entity 기준으로 테이블을 자동 생성합니다.

## Postman 시연 순서

발표에서는 다음 순서로 테스트하면 CRUD 흐름을 자연스럽게 보여줄 수 있습니다.

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

확인할 점:

- 요청한 책 정보가 그대로 저장됩니다.
- `id`는 서버에서 자동 생성됩니다.

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

확인할 점:

- 앞에서 등록한 책이 배열 형태로 조회됩니다.

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

확인할 점:

- URL의 id 값으로 특정 책 하나만 조회됩니다.

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

확인할 점:

- `id`는 유지되고 `name`, `price`, `author` 값만 변경됩니다.

### 5. 책 삭제

- Method: `DELETE`
- URL: `http://localhost:8080/books/1`

예상 응답:

```text
204 No Content
```

확인할 점:

- 삭제 성공 시 응답 본문 없이 `204 No Content`가 반환됩니다.

## 예외 처리 시연

존재하지 않는 id로 요청하면 `404 Not Found`가 반환됩니다.

```text
GET http://localhost:8080/books/999
PUT http://localhost:8080/books/999
DELETE http://localhost:8080/books/999
```

이 처리는 `BookService`에서 id로 책을 찾지 못했을 때 `ResponseStatusException`을 발생시키는 방식으로 구현했습니다.

## 발표 정리

이 프로젝트는 Spring Boot의 기본적인 REST API 개발 흐름을 실습하기 위한 예제입니다.

핵심은 다음 세 가지입니다.

- HTTP Method에 맞게 CRUD API를 설계했습니다.
- Controller, Service, Repository, Entity로 계층을 분리했습니다.
- JPA와 H2를 사용해서 별도 DB 설치 없이 데이터를 저장하고 테스트할 수 있게 만들었습니다.
