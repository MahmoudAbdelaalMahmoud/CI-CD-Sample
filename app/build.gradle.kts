import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("com.google.gms.google-services")
}

fun versionCode(): Int {
    val fileProp = file("../gradle.properties")
    var versionCode = 0
    if (fileProp.canRead()) {
        val prop = Properties()
        prop.load(fileProp.inputStream())
        versionCode = prop["VERSION_CODE"].toString().toInt() + 1
        prop["VERSION_CODE"] = versionCode.toString()
        prop.store(fileProp.writer(), null)
    }
    return versionCode

}


fun getProps(): Properties? {
    val fileProp = file("../version.properties")
    if (fileProp.canRead()) {
        val prop = Properties()
        prop.load(fileProp.inputStream())
        return prop
    }
    return null

}

fun getVersionCode(): Int {
    return getProps()?.run {
        this["majorVersion"].toString().toInt() * 1000
        +this["minorVersion"].toString().toInt() * 100
        +this["patchVersion"].toString().toInt()
    } ?: 0
}

fun getVersionName(): String {

    return getProps()?.run {
        " ${this["majorVersion"].toString()}.${this["minorVersion"].toString()}.${this["patchVersion"].toString()}"
    } ?: "1.0.0"
}


val VERSION_NAME: String by project
val VERSION_CODE: String by project
android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"
    defaultConfig {
        applicationId = "com.tridev.cicd_sample"
        minSdk = 21
        targetSdk = 30
        versionCode = getVersionCode()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}




dependencies {

    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation(platform("com.google.firebase:firebase-bom:28.1.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
//tasks.register("increaseVersionCode") {
//    versionCode()
//}