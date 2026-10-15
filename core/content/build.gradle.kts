plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:planner"))
    testImplementation(libs.junit)
}

// Hands the podcast scripts to the render tool in tools/podcast, so they are
// written once, here, and never copied.
tasks.register<JavaExec>("exportPodcasts") {
    group = "podcast"
    description = "Writes the podcast scripts to tools/podcast/podcasts.json."
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.shahaabapps.core.content.PodcastExportKt")
    args(rootProject.file("tools/podcast/podcasts.json").absolutePath)
}
