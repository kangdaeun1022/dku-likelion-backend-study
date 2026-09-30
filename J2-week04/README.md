# 2학기 4주차: MySQL 데이터베이스 실습

「12가지 기술로 구현하는 APP서비스」 챕터 05의 CRUD, 제약 조건, INNER JOIN 실습을 정리했습니다.

| 파일 | 내용 | 실습 DB |
| --- | --- | --- |
| `article-crud.sql` | 게시글 생성·조회·수정·삭제, 열 추가 | `a1` |
| `constraints.sql` | `NOT NULL`, 기본 키, 자동 증가, 조회 조건 | `a2` |
| `inner-join.sql` | 부서·사원 관계와 `INNER JOIN` | `a5` |

MySQL에 접속할 수 있는 환경에서 저장소 루트 기준으로 각각 실행합니다.

```bash
mysql -u root -p < J2-week04/article-crud.sql
mysql -u root -p < J2-week04/constraints.sql
mysql -u root -p < J2-week04/inner-join.sql
```

각 파일은 시작할 때 해당 실습 DB를 삭제하고 다시 생성하므로, 같은 이름의 기존 DB에 중요한 데이터가 있다면 실행하지 마세요. 강의자료의 실습 흐름을 따르되 기존 행에 `NOT NULL` 열을 추가할 때는 값을 먼저 채우고, 부서 번호는 고정 숫자 대신 부서명으로 조회하여 넣었습니다.
