FROM maven:3.9-eclipse-temurin-17-alpine AS build

WORKDIR /build

# 创建 Maven 配置目录
RUN mkdir -p /root/.m2

# 复制 Maven 配置（相对路径根据你 docker build 的上下文定）
COPY settings.xml /root/.m2/settings.xml

# 先缓存依赖
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

RUN ls -l /build/target
FROM openjdk:17-slim

# 安装 curl（用于 Docker 健康检查）
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /build/target/springboot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]