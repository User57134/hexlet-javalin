FROM gradle:8.12.1-jdk21

WORKDIR /app

COPY . .

RUN ./gradlew clean --no-daemon
RUN ./gradlew bootJar --no-daemon

RUN ./gradlew build --no-daemon -x test

CMD ["./gradlew", "run"]
