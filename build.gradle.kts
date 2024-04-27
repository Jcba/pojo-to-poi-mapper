plugins {
    `maven-publish`
    `java-library`
    signing
}

group = "io.github.jcba"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.assertj:assertj-core:3.25.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "pojo-to-apache-poi-mapper"
            from(components["java"])

            pom {
                name = "POJO to POI mapper"
                description = "Maps annotated Java POJO objects to Apache POI Sheets"
                url = "https://github.com/Jcba/pojo-to-poi-mapper"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "jcba"
                        name = "Job Bakker"
                    }
                }
                scm {
                    connection = "scm:git:https://github.com/Jcba/pojo-to-poi-mapper.git"
                    developerConnection = "scm:git:ssh:git@github.com:Jcba/pojo-to-poi-mapper.git"
                    url = "https://github.com/Jcba/pojo-to-poi-mapper"
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}