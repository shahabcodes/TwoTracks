plugins {
    id("twotracks.android.application")
    id("twotracks.android.compose")
    id("twotracks.android.hilt")
    id("twotracks.android.firebase")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.shahaabapps.twotracks"

    defaultConfig {
        applicationId = "com.shahaabapps.twotracks"
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:planner"))
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
