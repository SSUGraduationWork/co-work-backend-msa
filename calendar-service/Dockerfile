FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/calendar-service-1.0.jar CalendarService.jar
ENTRYPOINT ["java", "-jar", "CalendarService.jar"]