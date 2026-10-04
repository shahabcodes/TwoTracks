import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Wires in the Firebase BOM + the Firebase SDKs used by the app (Auth + Firestore).
 * The google-services plugin is applied by the application module itself,
 * since it requires a google-services.json to be present.
 */
class AndroidFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                val bom = libs.findLibrary("firebase-bom").get()
                add("implementation", platform(bom))
                add("implementation", libs.findLibrary("firebase-auth").get())
                add("implementation", libs.findLibrary("firebase-firestore").get())
                add("implementation", libs.findLibrary("kotlinx-coroutines-play-services").get())
            }
        }
    }
}
