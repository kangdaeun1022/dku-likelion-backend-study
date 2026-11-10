# 검증 기록 — 2026-11-10

- Java 21 `./gradlew test bootJar`: 성공. JPA/URL 통합 테스트 3개, 실패 0.
- 실제 JAR 실행: `/actuator/health` UP, `/h2-console/` HTTP 200.
- 임시 H2 파일에 URL 저장 → 프로세스 종료 → 같은 DB로 재실행 → 동일 데이터 조회 성공. 검증 서버 종료함.
- `terraform init -backend=false` 및 `terraform validate`: 성공. AWS provider 6.72.0, 자원 생성 없이 구성 검증.
- `terraform fmt -check`: 성공.
- `docker compose --env-file .env.example config --quiet`: 성공. 컨테이너 기동 검증은 아님.
- 배포 스크립트와 user-data의 `bash -n`: 성공. 워크플로 예제 YAML 파싱 성공.

미실행: PostgreSQL 컨테이너/SQL 실행, Docker 이미지 빌드·GHCR 업로드, MySQL·Redis·NPM 기동, AWS/Fly 생성·plan·apply, 도메인 연결, 운영 배포, 무중단 전환과 세션 유지.

초기 검증에서 Redis 의존성이 개발 세션과 health까지 연결되는 문제를 발견했다. Redis 세션 자동 구성을 제외하고 운영에서만 명시적으로 활성화했으며, Redis health도 운영에서만 검사하도록 수정한 뒤 검증에 통과했다.
