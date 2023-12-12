FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
ENTRYPOINT ["java", "-jar", "-XX:UseSSE=3", "-XX:UseAVX=0", "DashboardService.jar"]