plugins {
  `java-library`
}

dependencies {
  // api(libs.pgpainless.core) {
  //   exclude(group = "org.slf4j", module = "slf4j-api")
  // }
  implementation(libs.slf4j.api)
  implementation(libs.pgpainless.core)
  implementation(libs.teavm.core)
}
