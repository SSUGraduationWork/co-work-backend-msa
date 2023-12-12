FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/dashboard-service-1.0.jar DashboardService.jar
ENTRYPOINT ["java", "-jar", "-XX:UseSSE=3", "-XX:UseAVX=0", "DashboardService.jar"]