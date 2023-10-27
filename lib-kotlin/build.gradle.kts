plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    targetHierarchy.default()

    android() {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }

        it.compilations.getByName("main").cinterops {
            val reaktor by creating {
                defFile(file("cpp/reaktor.def"))
                headers("cpp/Reaktor.h")
                extraOpts("-Xsource-compiler-option", "-std=c++20")
                extraOpts("-Xcompile-source", "cpp/Reaktor.cpp")
            }
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.experimental.ExperimentalNativeApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(project(":flatbuffers-kotlin"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("com.facebook.fbjni:fbjni:0.4.0")
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
    }
}


android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        externalNativeBuild {
            cmake {
                cFlags.addAll(listOf("-Wall", "-Werror", "-fexceptions", "-fPIC", "-frtti", "-DWITH_INSPECTOR=1"))
                arguments.addAll(listOf("-DCMAKE_VERBOSE_MAKEFILE=1", "-DANDROID_STL=c++_shared"))
                cppFlags.add("-std=c++17")
            }
        }
    }
    externalNativeBuild {
        cmake {
            path = file("cpp/CMakeLists.txt")
        }
    }
    namespace = "com.jetbrains.android"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        prefab = true
    }
}