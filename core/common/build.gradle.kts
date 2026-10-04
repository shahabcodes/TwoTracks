plugins {
    id("twotracks.android.library")
    id("twotracks.android.hilt")
}

android {
    namespace = "com.shahaabapps.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.datastore.preferences)
}
