package dev.shibasis.dependeasy.android

import dev.shibasis.dependeasy.Version
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget

class AndroidConfiguration(
    var targetModifier: KotlinAndroidTarget.() -> Unit = {},
    var dependencies: KotlinDependencyHandler.() -> Unit = {},
    var testDependencies: KotlinDependencyHandler.() -> Unit = {},
    var integrationTestDependencies: KotlinDependencyHandler.() -> Unit = {}
)
fun KotlinMultiplatformExtension.droid(
    configuration: AndroidConfiguration.() -> Unit = {}
) {
    val configure = AndroidConfiguration().apply(configuration)
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
        publishLibraryVariants("release", "debug")
        compilations.all {
            kotlinOptions {
                jvmTarget = Version.SDK.Java.asString
                // Java 11 StringConcat Bug
                freeCompilerArgs = listOf(
                    "-Xstring-concat=inline"
                )
            }
        }
        configure.targetModifier(this)
    }

    sourceSets {
        androidMain.dependencies {
            configure.dependencies(this)
            // bad idea todo shibasis fix
            implementation("com.facebook.fbjni:fbjni:0.2.2")
        }

//        val androidUnitTest by getting {
//            dependencies {
//                implementation(kotlin("test-junit"))
//                implementation("junit:junit:4.13.2")
//                configure.testDependencies(this)
//            }
//        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("androidx.test:core-ktx:1.5.0")
                implementation("junit:junit:4.13.2")
                implementation("androidx.test.ext:junit:1.1.5")
                implementation("androidx.test.ext:junit-ktx:1.1.5")
                implementation("androidx.test.espresso:espresso-core:3.5.1")
                configure.integrationTestDependencies(this)
            }
        }
    }
}