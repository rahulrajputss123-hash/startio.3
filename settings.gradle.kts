pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Start.io (StartApp) Official Maven Repository
        maven { url = uri("https://www.startapp.com/maven") }
    }
}

rootProject.name = "Start.ioVideoPlayer"
include(":app")
