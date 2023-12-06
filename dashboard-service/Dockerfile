FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/*.jar DashboardService.jar
ENTRYPOINT ["java","-jar","DashboardService.jar"]