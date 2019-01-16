FROM java:8-jre
VOLUME /tmp

ARG DEPENDENCY=target/dependency

COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","nl.globalorange.compliancewise.config.ConfigApplication"]

HEALTHCHECK --interval=30s --timeout=30s --retries=10 CMD curl -f http://localhost:8888/actuator/health || exit 1

EXPOSE 8888