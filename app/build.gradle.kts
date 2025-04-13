plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// Carregar as propriedades do arquivo local.properties
val localProps = rootProject.file("local.properties").reader().useLines { lines ->
    lines.map { it.split("=") }
        .filter { it.size == 2 }
        .associate { it[0] to it[1] }
}

android {
    namespace = "com.example.vista"
    compileSdk = 35

    packaging {
        resources {
            excludes += setOf("META-INF/NOTICE.md", "META-INF/LICENSE.md")
        }
    }

    defaultConfig {
        applicationId = "com.example.vista"
        minSdk = 22
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // 👇 Adiciona essas linhas
        buildConfigField("String", "EMAIL_USER", "\"${localProps["emailUser"] ?: ""}\"")
        buildConfigField("String", "EMAIL_PASS", "\"${localProps["emailPass"] ?: ""}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true // Habilita o uso do BuildConfig
    }
}


dependencies {


    implementation (libs.material) // Ou a versão mais recente
    implementation(libs.androidx.ui.v154)
    implementation(libs.androidx.ui.text)
    testImplementation(libs.junit) // JUnit padrão
    androidTestImplementation(libs.androidx.junit.v115) // AndroidX test
    androidTestImplementation(libs.androidx.espresso.core.v351)
    implementation (libs.ucrop)
    implementation(libs.coil.compose)
    implementation (libs.androidx.material.icons.extended)
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.androidx.work.runtime.ktx)

    implementation (libs.android.mail)
    implementation (libs.android.activation)

    // Versão do Compose
    implementation(libs.androidx.foundation)  // androidx.compose.foundation:foundation
    implementation(libs.androidx.ui)          // androidx.compose.ui:ui
    implementation(libs.accompanist.swiperefresh)  // com.google.accompanist:accompanist-swiperefresh
    implementation(libs.threetenabp)          // com.jakewharton.threetenabp:threetenabp
    implementation(libs.androidx.core.ktx)    // androidx.core:core-ktx
    implementation(libs.androidx.appcompat)   // androidx.appcompat:appcompat
    implementation(libs.xmlpull)              // xmlpull:xmlpull
    implementation(platform(libs.androidx.compose.bom))  // Define a plataforma do Compose
    implementation(libs.androidx.runtime)     // androidx.compose.runtime:runtime
    implementation(libs.androidx.lifecycle.runtime.ktx)  // androidx.lifecycle:lifecycle-runtime-ktx
    implementation(libs.androidx.activity.compose)  // androidx.activity:activity-compose
    implementation(libs.androidx.ui.graphics)  // androidx.compose.ui:ui-graphics
    implementation(libs.ui.tooling.preview)    // androidx.compose.ui:ui-tooling-preview

    // Removido a dependência de material tradicional e corrigido a dependência de material3
    implementation(libs.androidx.material3)    // androidx.compose.material3:material3
    implementation(libs.androidx.material)     // androidx.compose.material:material

    testImplementation(libs.junit)                     // junit:junit
    androidTestImplementation(libs.androidx.junit)     // androidx.test.ext:junit
    androidTestImplementation(libs.androidx.espresso.core) // androidx.test.espresso:espresso-core
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Compose BOM for tests
    androidTestImplementation(libs.androidx.ui.test.junit4) // androidx.compose.ui:ui-test-junit4
    debugImplementation(libs.androidx.ui.tooling)  // androidx.compose.ui:ui-tooling
    debugImplementation(libs.androidx.ui.test.manifest)  // androidx.compose.ui:ui-test-manifest
}
