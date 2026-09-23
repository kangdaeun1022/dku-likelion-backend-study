# 멋사 챕터 04 — Spring Boot를 Fly.io에 자동 배포하기

2학기 3주차 과제: 새로운 Spring Boot 프로젝트를 만들고 GitHub `main` 브랜치에 push할 때마다 Fly.io에 자동 반영합니다.

## 구성

- Java 21 / Spring Boot 4.0.8 / Gradle Wrapper 9.7.1
- Spring MVC: `GET /` → HTTP 200, `Fly.io deployment successful!`
- Java 21 Gradle 빌드 이미지 → GraalVM Java 21 실행 이미지
- GitHub Actions: 빌드·테스트 → Fly 원격 빌드·배포 → 공개 HTTPS 응답 검사
- Fly 앱 이름: `likelion-cicd-kangdaeun1022`
- 배포 대상 URL: https://likelion-cicd-kangdaeun1022.fly.dev/

## 요청 처리 및 배포 흐름

```text
코드 수정 → main push → GitHub Actions
                        ├─ Java 21로 테스트·JAR 빌드
                        ├─ Fly에서 Docker 이미지 빌드
                        ├─ 머신에 배포 및 GET / 상태 검사
                        └─ 공개 HTTPS 성공 응답 검사

브라우저 → Fly HTTPS → 앱의 8080 포트 → HomeController
```

`main` 브랜치에서 `2-week03/` 파일을 변경한 push가 배포를 시작합니다. 테스트가 실패하면 배포하지 않습니다. 다른 주차의 파일을 바꿔도 이 앱은 재배포되지 않습니다.

## 로컬 실행

Java 21을 설치한 환경에서:

```bash
./gradlew clean build
./gradlew bootRun
curl http://localhost:8080/
```

예상 응답:

```text
Fly.io deployment successful!
```

운영 프로필로 JAR 실행:

```bash
java -Dspring.profiles.active=prod -jar build/libs/app.jar
```

## 주요 파일

| 파일 | 역할 |
| --- | --- |
| `src/main/java/com/likelion/flycicd/HomeController.java` | GET / 성공 응답 |
| `src/test/java/com/likelion/flycicd/HomeEndpointTests.java` | 실제 HTTP 서버의 상태 코드·본문·응답 형식 검증 |
| `Dockerfile` | Java 21로 테스트·빌드 후 실행 이미지 생성 |
| `fly.toml` | 앱 이름, 도쿄 리전, 8080 포트, 상태 검사, 머신 사양 |
| `.github/workflows/2-week03-fly-deploy.yml` | `2-week03/` 변경 push 시 배포 및 공개 URL 검증 |

## Fly.io 및 GitHub 최초 연결

Fly.io 계정에 결제 수단 또는 크레딧 설정이 필요합니다. 다음은 같은 프로젝트를 다시 설정할 때의 절차이며, 이미 생성된 앱은 재생성하지 않습니다.

```bash
fly auth login
fly launch --copy-config --no-deploy
fly deploy --remote-only --ha=false
```

GitHub 저장소의 `Settings → Secrets and variables → Actions`에 `FLY_API_TOKEN`을 등록합니다. 앱 하나에만 접근하는 배포 토큰을 사용합니다. 다음 명령은 토큰을 화면이나 파일에 출력하지 않고 GitHub secret으로 전달합니다.

`FLY_API_TOKEN`은 이 저장소에 이미 등록되어 있습니다. 토큰을 다시 만들 때는 저장소 루트에서 다음을 실행합니다.

```bash
set -o pipefail
fly tokens create deploy --app likelion-cicd-kangdaeun1022 --expiry 720h |
  gh secret set FLY_API_TOKEN --repo kangdaeun1022/dku-likelion-backend-study
```

토큰 유효기간은 30일입니다. 만료되면 같은 절차로 갱신합니다. 이 앱은 DB나 외부 API의 비밀 설정이 없으므로 `APPLICATION_SECRET_YML`은 필요하지 않습니다.

`2-week03/` 안의 파일을 바꾸고 `main`으로 push하면 저장소 루트의 `2-week03 Fly Deploy` 워크플로가 배포합니다. 결과는 `Actions`에서 확인할 수 있습니다.

## 챕터 자료에서 조정한 점

- 자료의 `container-registry.oracle.com/graalvm/jdk:21`은 익명 Docker 이미지 조회에서 401을 반환했습니다. [GraalVM 공식 안내](https://www.graalvm.org/jdk21/docs/getting-started/container-images/)의 `ghcr.io/graalvm/jdk-community:21`로 실행 이미지만 바꾸었습니다. Java 21 및 2단계 빌드 구조는 유지합니다.
- Java 실행 옵션은 `java -Dspring.profiles.active=prod -jar app.jar` 순서로 명확히 배치했습니다.
- 빌드할 때마다 wrapper를 새로 만드는 대신 저장소의 wrapper로 동일한 Gradle 버전을 사용합니다.
- `bootJar` 이름을 `app.jar`로 고정하고 일반 JAR 생성을 꺼, 여러 JAR이 Docker COPY에 잡히는 문제를 방지합니다.
- 과제 폴더인 `2-week03/` 변경만 대상으로 삼도록 저장소 루트 워크플로에 `paths` 필터를 두었습니다.
- Fly만 배포하므로 GitHub 권한은 `contents: read`면 충분합니다.
- 학습용 512MB shared CPU 머신 1개를 사용합니다. 요청이 없으면 정지하고 요청 시 시작합니다. 첫 요청이 느릴 수 있고, 머신 1개이므로 무중단 배포를 보장하지 않습니다.
- 자동 정지는 비용을 줄이는 설정이며, 무료를 의미하지 않습니다.

## 검증 기록

- Java 21 `./gradlew clean build`: 성공, 테스트 2개 통과.
- HomeController 구현 전 실제 HTTP 테스트가 404로 실패하고 구현 후 통과하는 것을 확인.
- 운영 프로필 JAR를 직접 실행: `GET /` HTTP 200, `text/plain;charset=UTF-8`, 성공 문자열 확인.
- `fly config validate`: 유효한 설정 확인.
- 빌드·실행 이미지의 linux/amd64 manifest 접근 확인.
- Fly 앱 `likelion-cicd-kangdaeun1022` 실제 생성 및 최초 배포 완료 (Tokyo, 512MB shared CPU 머신 1개).
- GitHub Actions secret `FLY_API_TOKEN` 등록 완료. 앱 범위 배포 토큰이며, 만료 기간은 30일입니다.
- 공개 URL https://likelion-cicd-kangdaeun1022.fly.dev/ 응답 확인: HTTP 200, `text/plain;charset=UTF-8`, `Fly.io deployment successful!`.
- Fly 헬스 체크: `servicecheck-00-http-8080` passing.
- 이 저장소의 `2-week03/` 변경을 `main`에 push하면 루트의 GitHub Actions가 테스트, Fly 배포, 공개 URL 검증을 수행합니다. 실행 결과는 [이 저장소의 Actions](https://github.com/kangdaeun1022/dku-likelion-backend-study/actions)에서 확인할 수 있습니다.

## 참고

- 제공된 챕터 04의 스텝 04-10, 04-11, 04-13 및 최종 요구사항을 기준으로 구현했습니다.
- [Fly 공식 GitHub Actions 배포 안내](https://fly.io/docs/launch/continuous-deployment-with-github-actions/)
- [Fly 앱 전용 배포 토큰](https://fly.io/docs/flyctl/tokens-create-deploy/)
