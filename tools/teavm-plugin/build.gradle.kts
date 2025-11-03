plugins {
  id("java-common-conventions")
  `java-library`
}

dependencies {
  implementation(libs.slf4j.api)
  compileOnly(libs.teavm.core)
  compileOnly(libs.teavm.jso.apis)
  //teavm(teavm.libs.jsoApis)
}
