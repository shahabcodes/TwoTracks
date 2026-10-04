import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

/**
 * Convention plugin for the application module.
 * Applies `com.android.application` + Kotlin and shares the common Android config.
 *
 * Versioning: `version.properties` at the repo root is the source of truth for
 * versionName. CI overrides versionCode with its run number (APP_VERSION_CODE)
 * so every build from git is uniquely numbered and installs over the previous one.
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            val versionProps = Properties().apply {
                val file = rootProject.file("version.properties")
                if (file.exists()) file.inputStream().use { load(it) }
            }
            val fileVersionName = versionProps.getProperty("versionName", "0.1.0")
            val fileVersionCode = versionProps.getProperty("versionCode", "1").toIntOrNull() ?: 1

            // Shared debug keystore so every build (local or CI) signs with the same
            // key; otherwise updates fail with "App not installed" (signature mismatch).
            val sharedDebugKeystore = rootProject.file("keystore/debug.keystore")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig {
                    targetSdk = 35
                    versionCode = System.getenv("APP_VERSION_CODE")?.toIntOrNull() ?: fileVersionCode
                    versionName = System.getenv("APP_VERSION_NAME") ?: fileVersionName
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
                if (sharedDebugKeystore.exists()) {
                    signingConfigs.getByName("debug") {
                        storeFile = sharedDebugKeystore
                        storePassword = "android"
                        keyAlias = "androiddebugkey"
                        keyPassword = "android"
                    }
                }
                buildTypes {
                    release {
                        isMinifyEnabled = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro",
                        )
                    }
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
            }
        }
    }
}
