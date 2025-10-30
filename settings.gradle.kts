plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "pgp"

include("tools:teavm-plugin")
include("lib")
