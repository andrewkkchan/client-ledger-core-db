FROM openjdk:11
RUN apt-get update && apt-get install -y maven
COPY . /project
RUN  cd /project && mvn package
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/project/target/client-ledger-core-db-1.0.0-SNAPSHOT.jar"]
