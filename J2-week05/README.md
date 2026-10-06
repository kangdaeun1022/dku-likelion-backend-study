# 2학기 5주차 — Spring Data JPA 영속성 실습

챕터 06의 06-01~06-20을 따라, [8주차 URL 단축 서비스](../week08/demo03)의 메모리 저장을 MySQL + Spring Data JPA로 바꾼 실습입니다. Java 21과 저장소의 기존 Spring Boot 4.1.1/Gradle 구성을 사용합니다.

## 실습 내용

| 강의 단계 | 구현 |
| --- | --- |
| 06-03~06-05 | JPA·MySQL 의존성, DB 연결, 엔티티 테이블 생성, SQL 로그 |
| 06-06~06-10 | 게시글 생성·개수·삭제·수정·조회 및 메서드 이름 기반 검색 |
| 06-11~06-13 | `ArticleService`, 변경 감지, 날짜 자동 기록, `RsData` |
| 06-14~06-17 | 회원 2명 초기화, 중복 확인, `GlobalException`, 트랜잭션, `@MappedSuperclass` |
| 06-18~06-20 | 게시글·단축 URL 작성자 연결, `SurlService`, 요청별 `Rq`, JPA 프록시 |

`BaseEntity`는 자동 증가 ID를, `BaseTime`은 생성·수정 시각을 공통으로 제공합니다. `Article`과 `Surl`은 `Member`를 작성자로 참조합니다. `ArticleService`의 수정과 `SurlService`의 조회수 증가는 트랜잭션 안에서 값을 바꾸고 JPA 변경 감지로 저장됩니다. 샘플 회원 `user1`, `user2`와 게시글 2개는 DB가 비어 있을 때만 추가됩니다.

## 로컬 실행

Java 21과 MySQL을 준비한 뒤 **실습 전용 DB**를 만듭니다. 아래 SQL은 기존 DB를 삭제하지 않습니다.

```bash
mysql -u root -p < database/create-database.sql
```

DB 접속 정보는 환경 변수 `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`로 설정합니다. 기본 URL은 `jdbc:mysql://localhost:3306/surl_dev?useUnicode=true&characterEncoding=utf8`, 기본 사용자 이름은 `root`입니다. 비밀번호는 코드에 저장하지 않습니다.

```bash
./gradlew test
./gradlew bootRun
```

브라우저 또는 터미널에서 `http://localhost:8080/all`을 열어 저장된 URL을 확인할 수 있습니다. 아래 예시는 **샘플 회원 선택 기능**을 이용합니다. 이 기능은 비밀번호를 검증하는 실제 로그인 기능이 아니므로 공개 서비스에 사용하면 안 됩니다.

```bash
curl -c cookies.txt -X POST -d 'username=user1' http://localhost:8080/session/login
curl -b cookies.txt -G --data-urlencode 'body=멋사' \
  --data-urlencode 'url=https://likelion.university/' http://localhost:8080/add
curl http://localhost:8080/all
curl -i http://localhost:8080/g/1
```

`/add`는 이전 8주차 실습의 GET 주소를 유지했습니다. 실제 서비스라면 인증을 구현하고 등록 요청을 POST로 바꿔야 합니다. `spring.jpa.hibernate.ddl-auto=update` 역시 강의의 로컬 실습 설정이며 운영 DB에는 그대로 적용하지 않습니다.

## 검증

- `./gradlew test`: H2의 MySQL 호환 모드에서 게시글 CRUD·검색, 회원 중복 예외, 작성자 연결, 조회수 증가, 세션 회원의 URL 등록을 검증했습니다.
- 임시 MySQL에서 앱 실행 → 샘플 회원 2명·게시글 2개 및 URL 1개 저장 → 앱 재시작 후 URL 유지 및 샘플 중복 없음 확인.
- `/g/1`은 HTTP 302로 원래 URL에 이동하며 MySQL의 조회수 값이 1로 증가하는 것을 확인했습니다.
