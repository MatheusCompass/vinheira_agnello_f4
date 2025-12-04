// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // Ensure Hilt plugin is available by declaring explicitly (fallback)
    id("com.google.dagger.hilt.android") version "2.47" apply false
}

// Force a specific JavaPoet version used by Hilt's annotation processors to avoid
// runtime NoSuchMethodError (com.squareup.javapoet.ClassName.canonicalName())
configurations.all {
    resolutionStrategy.force("com.squareup:javapoet:1.13.0")
}
