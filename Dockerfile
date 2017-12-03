FROM gradle as builder
ADD src/ /home/gradle/project/src/
ADD build.gradle /home/gradle/project/build.gradle
WORKDIR /home/gradle/project/
USER root
RUN chmod -R 777 .
USER gradle
RUN gradle clean build

FROM openjdk:8-jre-alpine
COPY --from=builder /home/gradle/project/build/libs/auditsystem-0.0.1-SNAPSHOT.jar /usr/bin/auditsystem-0.0.1-SNAPSHOT.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://audit-database:5432/postgres \
    SPRING_DATASOURCE_USERNAME=postgres \
    SPRING_DATASOURCE_PASSWORD=postgres \
    SPRING_JPA_GENERATE-DDL=true \
    ROLE_URL=http://rolesystem:8084 \
    AUDIT_URL=http://auditsystem:8086 \
    ROLE_USERNAME=system
EXPOSE 8086
CMD ["java", "-jar", "/usr/bin/auditsystem-0.0.1-SNAPSHOT.jar"]
