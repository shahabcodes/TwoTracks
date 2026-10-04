plugins {
    id("twotracks.android.library")
    id("twotracks.android.hilt")
    id("twotracks.android.firebase")
}

android {
    namespace = "com.shahaabapps.core.data"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        // Point the app at the local Firebase emulator suite with:
        //   ./gradlew :app:assembleDebug -PfirebaseEmulator=true
        // (auth on 9099, firestore on 8080, reached from the Android emulator via 10.0.2.2)
        val useEmulator = (project.findProperty("firebaseEmulator") as String?) == "true"
        buildConfigField("boolean", "USE_FIREBASE_EMULATOR", useEmulator.toString())
        buildConfigField("String", "FIREBASE_EMULATOR_HOST", "\"10.0.2.2\"")
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:seed"))
    implementation(libs.kotlinx.coroutines.android)
}
