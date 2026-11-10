# 2학기 6주차 — 챕터 07: Terraform, AWS EC2와 CI/CD

공지에서 허용한 **로컬 실습 + 클라우드 실습 절차 정리** 방식으로 제출합니다. J2-week05의 JPA 프로젝트를 이어서 개발·운영 DB 설정과 배포 파일을 작성했습니다. AWS/Fly 유료 자원, GHCR 이미지, 도메인은 실제 생성하지 않았습니다. 따라서 클라우드 배포나 무중단 배포에 성공했다고 주장하지 않습니다.

## 구성과 학습 내용

| 강의 | 구현/정리 |
| --- | --- |
| 07-02~04 | PostgreSQL 드라이버·SQL·Compose, 로컬 H2 파일 DB와 콘솔 |
| 07-05~08 | Fly PostgreSQL·프록시·운영 DB·도메인·자원 정리 절차 |
| 07-09~13 | IAM·CLI 개념, Terraform VPC/공개 서브넷/EC2/SSM 역할 코드 |
| 07-14~17 | GHCR 로그인, MySQL·Redis·Nginx Proxy Manager Compose |
| 07-18~20 | Dockerfile, 수동 교체 스크립트, GHCR/SSM 자동화 예제, 배포 진단 및 무중단 배포 절차 |

JPA는 JDBC 드라이버를 통해 DB에 접근합니다. 드라이버와 접속 설정을 바꾸면 같은 엔티티를 다른 DB에서 사용할 수 있지만 자료형·예약어·SQL 차이는 별도로 검증해야 합니다. Redis 세션은 여러 앱 인스턴스가 세션을 공유하게 합니다. Redis를 설치하는 것만으로 세션 공유가 되지는 않으므로 운영 프로필에 `@EnableRedisHttpSession`을 적용했습니다.

## 로컬 실행

Java 21에서 이 폴더를 열고 실행합니다.

```bash
./gradlew test bootJar
./gradlew bootRun
```

기본 `dev`는 `jdbc:h2:file:./data/surl_dev`에 저장합니다. `/h2-console`에서 같은 URL과 사용자 `sa`, 빈 비밀번호로 접속합니다. `/all`에서 URL 목록, `/actuator/health`에서 상태를 확인합니다. 데이터 파일은 Git에서 제외했습니다. 테스트는 별도 메모리 H2를 사용합니다.

PostgreSQL 실습:

```bash
cp .env.example .env
# .env의 예시 비밀번호를 각자 값으로 변경
 docker compose --profile postgres up -d postgres
export DB_PASSWORD='자신의 로컬 DB 비밀번호'
./gradlew bootRun --args='--spring.profiles.active=postgres'
```

`postgresql-practice.sql`은 DBeaver 등에서 `surl_dev`에 접속해 실행합니다. PostgreSQL은 `USE` 대신 접속 DB를 선택하고, 관리자 기본 이름은 `postgres`입니다. 이 Compose는 실습 전용 사용자 `surl`을 만듭니다. 운영 `prod`는 환경 변수로 MySQL 접속 정보를 받고, H2 콘솔과 SQL 출력, 샘플 회원 초기화·샘플 회원 선택 API를 사용하지 않습니다.

## AWS·Terraform 절차 — 미실행

1. AWS 계정을 준비하고 루트 계정 MFA를 설정합니다. IAM은 사용자/역할에 정책을 연결하여 권한을 부여합니다. 일상적인 작업에는 루트 계정을 사용하지 않습니다. 가능하면 IAM Identity Center의 임시 자격 증명으로 `aws configure sso`, `aws sso login`을 사용합니다. 강의 방식의 액세스 키를 쓴다면 코드에 넣지 않고 개인 AWS 프로필에만 설정합니다.
2. `aws sts get-caller-identity`로 계정 확인. Terraform AWS provider는 AWS API를 호출하며 AWS CLI를 내부 실행하는 도구는 아닙니다.
3. `cd terraform` 후 `terraform init`, `terraform validate`, `terraform plan`으로 변경을 검토합니다. `plan`은 자원을 만들지 않지만 실제 계정 조회·권한 검증이 필요합니다. 이번 제출에서는 계정에 접근하는 plan/apply는 실행하지 않았습니다.
4. 비용과 생성 대상을 확인한 경우에만 `terraform apply`를 실행합니다. EC2는 Amazon Linux 2023, 기본 t3.small, 암호화 EBS 20GB입니다. SSH 키 없이 SSM으로 접속합니다. DB·Redis·관리 포트는 인터넷에 공개하지 않습니다.
5. `terraform output instance_id` 확인 후 Session Manager로 접속합니다. user-data는 Docker·Git·SSM만 준비합니다. Compose plugin은 Docker 공식 설치 안내에 따라 별도 설치하고 `docker compose version`을 확인합니다.

Terraform의 state는 자원 ID와 민감한 값을 포함할 수 있어 제외했습니다. provider lock 파일은 버전 재현을 위해 커밋합니다. 로컬 state를 잃으면 생성 자원을 관리하기 어려우므로 개인 보관이 필요합니다.

## MySQL·Redis·프록시와 수동 배포 — 미실행

1. EC2의 `/opt/likelion`에 `docker-compose.yml`, `infra/deploy.sh`를 복사합니다. `.env.example`에서 개인 `.env`를 만들고 DB/Redis 비밀번호를 지정합니다. Git·이미지에는 `.env`를 넣지 않습니다.
2. `docker compose up -d mysql redis proxy`로 기동합니다. MySQL은 별도 앱 사용자와 볼륨, Redis는 비밀번호와 AOF 볼륨을 사용합니다. Nginx Proxy Manager의 81번 관리 포트는 호스트 루프백에만 연결했습니다. SSM 포트 포워딩으로 관리 화면에 접속해 계정을 설정합니다.
3. 로컬 `docker build -t likelion-surl .`로 이미지를 만들거나 GHCR 이미지를 받습니다. 비공개 이미지는 EC2에서 `read:packages` 토큰을 `docker login ghcr.io --username 사용자 --password-stdin`으로 전달합니다. 토큰을 명령줄 인자로 넣거나 이미지 안에 비밀 설정을 복사하지 않습니다.
4. 처음의 빈 운영 DB에는 스키마가 없습니다. 운영 기본값 `ddl-auto=validate`는 테이블을 만들지 않습니다. 별도 마이그레이션을 적용하거나, 이 실습에서는 DB 백업 후 1회만 `docker compose --profile production run --rm -e SPRING_JPA_HIBERNATE_DDL_AUTO=update app`을 실행해 테이블 생성 완료 후 종료합니다. 이후 기본 validate 설정으로 실행합니다.
5. `docker compose --profile production up -d app`. NPM에서 Proxy Host의 Forward Host는 `app`, 포트는 `8080`으로 설정합니다. 두 컨테이너는 Compose 네트워크에서 통신합니다.
6. DNS A 레코드를 EC2 공인 IP로 지정하고 NPM에서 인증서를 발급합니다. 기존 Fly CNAME을 EC2 A 레코드로 바꿀 때 전파를 기다립니다. EC2 기본 공인 IP는 재할당될 수 있습니다. Elastic IP를 쓴다면 비용·반납도 확인합니다.

## CI/CD와 무중단 배포

`ci/publish.yml.example`은 main 변경 → Java 테스트 → Docker 빌드 → 커밋 SHA 태그로 GHCR 업로드 → OIDC 역할로 AWS SSM 배포 요청 → 원격 결과 대기의 예제입니다. 현재 예제 파일이라 GitHub Actions에서 실행되지 않습니다. 활성화하려면 저장소 루트 `.github/workflows/week06.yml`로 옮기고 AWS OIDC 신뢰 정책을 이 저장소/main에 제한한 배포 역할과 `AWS_DEPLOY_ROLE_ARN`, `EC2_INSTANCE_ID`를 준비해야 합니다. 역할에는 지정 인스턴스의 SSM SendCommand 및 결과 조회 권한을 부여합니다. EC2의 GHCR 로그인과 배포 파일 준비도 선행해야 합니다.

`infra/deploy.sh`는 **단일 컨테이너 교체** 예제여서 잠시 연결이 끊길 수 있습니다. 실패 시 이전 SHA 이미지를 인자로 다시 실행하여 복구합니다. 무중단 구성으로 확장하려면 다음 절차가 필요합니다.

1. 기존 blue를 유지하며 별도 이름의 green을 같은 내부 네트워크에 실행.
2. 제한 시간 내 green의 `/actuator/health`가 UP인지 확인. 실패하면 green만 제거하고 blue 유지.
3. 프록시 upstream을 green으로 변경하고 설정 검사 후 reload.
4. 외부 HTTPS 응답 확인 후 기존 요청을 처리할 유예 시간을 두고 blue 종료.
5. 전환 실패 시 프록시를 blue로 되돌림. Redis 세션과 이전/새 앱이 호환되는 DB 스키마가 전제.

강의의 socat 프로세스 강제 종료·무한 대기 방식은 요청 단절과 무기한 대기 가능성이 있어 그대로 실행하지 않았습니다. 프록시 전환과 요청 유예까지 실제 검증해야 무중단이라고 할 수 있습니다.

## Fly PostgreSQL·도메인 정리 — 미실행

강의에서는 `flyctl pg create`로 DB를 만들고 `flyctl proxy 5434:5432 -a DB앱이름`으로 로컬 5434를 연결합니다. 접속 시에는 프록시가 떠 있어야 하며 DBeaver도 로컬 포트 5434를 사용해야 합니다. 새 DB/운영 서비스 생성은 비용 확인 후 진행하고, 계정별 접속 정보는 개인 보관합니다. `fly certs add 도메인` 이후 안내된 DNS 레코드를 설정하고 인증서 상태를 확인합니다. 강의 당시 명령과 상품은 바뀔 수 있어 실제 생성 전 Fly 안내를 확인해야 합니다.

실습 종료 시 `docker compose down`으로 앱을 중단합니다. `down -v`는 DB 볼륨도 삭제하므로 백업 후 선택합니다. AWS는 같은 state에서 `terraform plan -destroy`, `terraform destroy`를 수행하고 EC2/EBS/IP 등 잔여 자원과 청구 내역을 확인합니다. Fly 앱·DB도 각각 확인하여 사용하지 않는 자원을 삭제합니다. 전체 `~/.aws` 삭제 대신 해당 프로필/액세스 키만 정리합니다.

## 오류 확인 순서

- 앱 시작 실패: `docker compose logs app` → DB/Redis 호스트·비밀번호·테이블 확인.
- NPM 502: 앱 상태와 내부 서비스 이름/8080 연결 확인. `localhost`는 NPM 자신을 가리킵니다.
- GHCR unauthorized: 패키지 공개 여부와 토큰 `read:packages` 권한 확인.
- SSM 미접속: 인스턴스 역할·SSM Agent·외부 통신 확인.
- 인증서 실패: DNS 전파와 80/443 접근 확인.
- CI 성공인데 배포 실패: SSM 요청 접수와 실제 명령 성공은 다릅니다. waiter와 원격 출력 확인.

## 검증 결과

검증 결과는 실제 도구 실행 후 `VERIFICATION.md`에 기록했습니다. 클라우드 apply, 이미지 업로드, 공개 HTTPS, Redis 세션 연속성 및 무중단 전환은 미실행입니다.

참고: [Terraform validate](https://developer.hashicorp.com/terraform/cli/commands/validate), [AWS CLI SSO](https://docs.aws.amazon.com/cli/latest/userguide/cli-configure-sso.html), [GitHub 이미지 게시](https://docs.github.com/en/actions/tutorials/publish-packages/publish-docker-images).
