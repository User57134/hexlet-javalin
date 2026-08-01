
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("java")
    application
}

application {
    mainClass.set("org.example.hexlet.HelloWorld")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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
