FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn -B clean package

FROM jetty:12-jre17-eclipse-temurin
COPY --from=build /app/target/email-servlet.war /var/lib/jetty/webapps/ROOT.war

ENV PORT=8080
EXPOSE 8080

CMD ["sh", "-c", "java -jar /usr/local/jetty/start.jar jetty.http.port=${PORT:-8080}"]
