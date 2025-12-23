// In your root project folder: settings.gradle.kts

pluginManagement {
    repositories {
        google()
        mavenCentral() // Make sure this is here
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral() // <-- THIS IS THE FIX. Add this line.
    }
}

rootProject.name = "swiftdrop"
include(":app")
