plugins {
    id("twotracks.android.library")
    id("twotracks.android.compose")
}

android {
    namespace = "com.shahaabapps.core.ui"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.coil.compose)
    testImplementation(libs.junit)
}
