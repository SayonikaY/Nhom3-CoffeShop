plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.microsoft.sqlserver:mssql-jdbc:12.10.0.jre11")
    implementation("org.hibernate.orm:hibernate-core:6.4.0.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

javafx {
    version = "21"
    modules("javafx.controls", "javafx.fxml")
}

application {
    mainClass = "nhom03.App"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>{
    options.encoding = "UTF-8"
}

tasks.withType<Test> {
    systemProperty("file.encoding", "UTF-8")
}
