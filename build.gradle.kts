plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.57.1" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.9.4" apply false
}
