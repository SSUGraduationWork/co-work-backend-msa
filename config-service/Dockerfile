FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/config-service-1.0.jar ConfigService.jar
ENTRYPOINT ["java", "-jar", "-XX:UseSSE=3", "-XX:UseAVX=0","ConfigService.jar"]