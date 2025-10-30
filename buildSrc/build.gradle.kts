plugins {
  `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}
