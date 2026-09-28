FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests clean package

FROM jetty:12-jre17-eclipse-temurin
COPY --from=build /app/target/email-servlet.war /var/lib/jetty/webapps/ROOT.war

EXPOSE 10000

CMD ["sh", "-c", "java -jar /usr/local/jetty/start.jar jetty.http.host=0.0.0.0 jetty.http.port=${PORT:-10000}"]
