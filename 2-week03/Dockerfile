# 챕터 04의 Java 21 Gradle/GraalVM 2단계 빌드
FROM gradle:jdk21-graal-jammy AS builder
WORKDIR /app

# 저장소의 wrapper로 로컬과 배포에 동일한 Gradle 버전을 사용한다.
COPY gradlew build.gradle settings.gradle ./
COPY gradle gradle
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew build --no-daemon

# 자료의 Oracle Registry 주소는 Docker 조회에서 401을 반환하므로
# 공식 GraalVM Community Java 21 공개 이미지를 사용한다.
FROM ghcr.io/graalvm/jdk-community:21
WORKDIR /app
COPY --from=builder /app/build/libs/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=65", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
