
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
}

application {
    mainClass.set("org.example.hexlet.App")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Подключение библиотеки для проверки на безопасность html
    implementation("com.googlecode.owasp-java-html-sanitizer:owasp-java-html-sanitizer:20260313.1")

    // Подключение еще одной библиотеки для проверки на безопасность html
    implementation("org.apache.commons:commons-text:1.15.0")

    // Подключаем Javalin
    implementation("io.javalin:javalin:6.1.3")

    // Подключаем логи для Javalin
    implementation("org.slf4j:slf4j-simple:2.0.7")

    // Подключаем модуль рендеринга для Javalin,
    // implementation означает, что библиотека будет упакована внутрь вашего готового приложения
    // и будет доступна как при компиляции, так и во время работы
    implementation("io.javalin:javalin-rendering:6.1.3")

    // Подключаем шаблонизатор Jte для Javalin
    implementation("gg.jte:jte:3.1.9")

    // Подключаем библиотеку Lombook
    implementation("org.projectlombok:lombok:1.18.46")

    // Библиотека Lombok используется только в момент компиляции исходного кода,
    // в готовой сборке присутствовать не будет
    compileOnly("org.projectlombok:lombok:1.18.46")

    // Устанавливаем, что разбор аннотаций будет производить Lombok
    annotationProcessor("org.projectlombok:lombok:1.18.46")

    // Подключение базы данных H2
    implementation("com.h2database:h2:2.2.220")

    // Подключение пуллера потокво для БД
    implementation("com.zaxxer:HikariCP:5.0.1")

    // Подключение драйвера Postgresql
    implementation("org.postgresql:postgresql:42.7.3")

    // Подключается для тестирования Javalin
    testImplementation("io.javalin:javalin-testtools:6.1.3")

    // Подключается для тестирования: assertThat
    testImplementation("org.assertj:assertj-core:3.26.3")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    // https://technology.lastminute.com/junit5-kotlin-and-gradle-dsl/
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        // showStackTraces = true
        // showCauses = true
        showStandardStreams = true
    }
}
