plugins {
  application
  java
}

group = "com.ada"
version = "0.1.0"

java {
  toolchain { languageVersion.set(JavaLanguageVersion.of(17)) }
}

repositories { mavenCentral() }

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test { useJUnitPlatform() }

// a classe Main precisa existir neste pacote
application {
  application { mainClass.set("com.ada.commerce.controllers.cli.Main") }
}

