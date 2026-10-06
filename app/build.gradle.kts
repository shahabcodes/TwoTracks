plugins {
    id("twotracks.android.application")
    id("twotracks.android.compose")
    id("twotracks.android.hilt")
    id("twotracks.android.firebase")
    alias(libs.plugins.google.services)
}

// The repo's CHANGELOG.md is bundled as an asset so the app can show "What's new"
// after an update and the full version history on its Changelog page.
val changelogAssets = layout.buildDirectory.dir("generated/changelog/assets")
val copyChangelog = tasks.register<Copy>("copyChangelog") {
    from(rootProject.file("CHANGELOG.md"))
    into(changelogAssets)
}

android {
    namespace = "com.shahaabapps.twotracks"

    defaultConfig {
        applicationId = "com.shahaabapps.twotracks"
    }

    buildFeatures {
        buildConfig = true
    }

    sourceSets.getByName("main").assets.srcDir(changelogAssets)
}

tasks.named("preBuild") { dependsOn(copyChangelog) }

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:planner"))
    implementation(project(":core:content"))
    implementation(project(":core:seed"))
    implementation(project(":core:common"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.coil.compose)
    implementation(libs.androidx.biometric)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
}
