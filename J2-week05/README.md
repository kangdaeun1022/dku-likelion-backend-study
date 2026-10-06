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

