import groovy.xml.dom.DOMCategory.attributes
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
}

group = "me.statuxia"
version = "1.0"

repositories {
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/releases/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("net.dv8tion:JDA:5.0.0-beta.17")
    implementation("org.json:json:20231013")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("kotyatkiBot")
        mergeServiceFiles()
        manifest {
            attributes(mapOf("Main-Class" to "me.statuxia.Main"))
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}