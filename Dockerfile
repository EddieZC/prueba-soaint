FROM openjdk:11.0.6-jdk
LABEL maintainer="developer"
WORKDIR /workspace
RUN ls -la /workspace
COPY /target/prueba*.jar prueba.jar
RUN ls -la /workspace
EXPOSE 8912
ENTRYPOINT exec java -jar /workspace/prueba.jar