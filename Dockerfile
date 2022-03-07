FROM openjdk:11-jdk

ARG WAR_FILE=build/libs/*.jar

COPY ${WAR_FILE} docker-springboot.jar

COPY entrypoint.sh entrypoint.sh
RUN chmod 774 entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]