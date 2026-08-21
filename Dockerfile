FROM gradle:8.7-jdk21

WORKDIR /app

COPY . .

RUN ./gradlew clean --no-daemon

RUN ./gradlew build --no-daemon -x test

CMD ["./gradlew", "run"]
