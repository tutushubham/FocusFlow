# Lessons (FocusFlow / StudyPartner)

## AGP 9 + Kotlin 2.1 `kotlin` extension conflict

**Symptom:** `Cannot add extension with name 'kotlin'` when applying `org.jetbrains.kotlin.android` on `:app`.

**Fix used:** Downgrade to **AGP 8.7.2** + **Kotlin 2.0.21** + matching **KSP** until the toolchain pair is verified with AGP 9.

## JVM toolchain vs local JDK

**Symptom:** Gradle could not provision JDK 17 toolchain on the build host.

**Fix used:** Remove strict `jvmToolchain(17)` from Android modules; use `compileOptions` + root `subprojects` `KotlinCompile` `jvmTarget = JVM_17`; JVM libraries use `sourceCompatibility`/`targetCompatibility` + explicit `KotlinCompile` jvmTarget instead of `java { toolchain { } }`.

## Feature modules referencing `core:model` enums

**Symptom:** `Cannot access class … StudyLevel / MatchStatus` from `feature:*` when only depending on `:core:domain` (model types are `implementation`, not `api`).

**Fix used:** Add `implementation(project(":core:model"))` where feature code imports `com.tutushubham.studypartner.model.*` (or switch domain to `api(project(":core:model"))` if you want transitive exposure).
