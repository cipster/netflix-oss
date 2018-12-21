FROM openjdk:8-jdk-alpine
VOLUME /tmp

ARG DEPENDENCY=target/dependency

COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","nl.globalorange.compliancewise.config.Application"]

HEALTHCHECK --interval=30s --timeout=30s CMD wget --quiet --tries=10 --spider http://localhost:8888/actuator/health/ || exit 1

EXPOSE 8888