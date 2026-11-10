# 2학기 7주차 — 챕터 08: 회원가입 REST API

첨부 자료의 08-01~08-16을 기준으로 챕터 7 프로젝트에서 회원가입 API를 구현했습니다. Java 21, Spring Boot 4.1.1, Spring Data JPA, Jakarta Validation을 사용합니다. 기본 실행은 H2 파일 DB여서 별도 DB 설치 없이 실습할 수 있습니다.

## 실행

```bash
./gradlew test
./gradlew bootRun
curl -i -X POST http://localhost:8080/api/v1/members \
  -H 'Content-Type: application/json' \
  -d '{"username":"new_user","password":"StrongPass12!","nickname":"아기사자"}'
```

성공하면 HTTP 201과 아래 양식을 반환합니다. 같은 아이디로 다시 요청하면 HTTP 409, 입력값 누락·형식 오류·잘못된 JSON은 HTTP 400입니다.

```json
{
  "resultCode": "201-1",
  "statusCode": 201,
  "msg": "회원가입이 완료되었습니다.",
  "data": {"id": 3, "username": "new_user", "nickname": "아기사자"}
}
```

오류도 `resultCode`, `statusCode`, `msg`, `data` 구조를 사용하며 오류의 data는 null입니다. 응답에는 비밀번호와 해시를 넣지 않습니다. DB에는 BCrypt 해시를 저장합니다. BCrypt 입력 바이트 제한을 넘지 않도록 실습 비밀번호는 영문/숫자/ASCII 기호 8~64자로 제한했습니다. 아이디는 영문/숫자/밑줄 3~30자, 닉네임은 공백만 허용하지 않으며 최대 100자입니다.

## 강의 단계별 구현

| 단계 | 내용 |
| --- | --- |
| 08-02~04 | Member에 비밀번호 해시·닉네임 추가, 서비스 가입, Ut.Str의 isBlank/hasLength, 예외 발생 |
| 08-05~08 | 공통 예외 처리, JSON API, 생성은 POST·자원 경로는 명사형 |
| 08-09~11 | `/api/v1/members`, 요청 record, `@RequestBody`, 상태 코드·성공/오류 응답 통일 |
| 08-12~14 | `@Valid`, `@NotBlank`, `@Size`, `@Pattern`, Validation·JSON·기타 예외 처리 |
| 08-15~16 | 비밀 정보를 제외한 응답 record, RsData.statusCode를 실제 HTTP 코드에 반영 |

08-16의 응답 코드 설정은 Aspect 대신 Spring MVC의 `ResponseBodyAdvice`를 사용했습니다. JSON 직렬화 직전에 RsData의 상태 코드를 HTTP 응답에 지정하는 역할입니다. 예외 응답도 같은 경로를 거칩니다. 프레임워크의 JSON 파싱 오류 등은 `ResponseEntityExceptionHandler`에서 같은 양식으로 변환합니다. 예상하지 못한 오류는 서버에 로그를 남기고 응답에는 내부 상세를 노출하지 않습니다.

가입 서비스는 트랜잭션 안에서 아이디 중복을 조회하고 저장합니다. DB의 unique 제약도 유지하여 동시에 같은 이름이 들어오는 경우를 방어합니다. DB 제약 오류 응답은 409로 처리합니다.

Postman에서는 POST, URL `/api/v1/members`, Body → raw → JSON을 선택하고 위 요청을 입력합니다. `requests.http`에도 성공/중복/누락 요청을 정리했습니다.

## 범위와 데이터

이 과제는 회원가입이며 실제 비밀번호 로그인·인증은 다음 구현 범위입니다. 이전 주차의 사용자 이름만 고르는 `/session/login` 샘플 API는 제거했습니다. 기본 dev는 `./data/surl_dev`에 저장하고 테스트는 격리된 메모리 H2를 사용합니다. 이 폴더는 이전 주차와 별도 앱으로 실행하며 이전 DB 파일을 복사하지 않습니다. 이미 회원 행이 있는 DB에 새 NOT NULL 필드를 도입한다면 별도 데이터 마이그레이션이 필요합니다.

## 검증

기존 JPA/URL 통합 테스트와 회원가입 테스트에서 HTTP 201, 중복 409, 누락/JSON 오류 400, BCrypt 해시 검증 및 응답 비밀번호 제외를 확인합니다. 실제 실행 결과는 `VERIFICATION.md`에 기록합니다.
