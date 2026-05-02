plugins {
    `kotlin-dsl`
}

// convention plugin 자체를 컴파일할 때 필요한 Gradle 플러그인 의존성만 선언
dependencies{
    implementation("org.springframework.boot:org.springframework.boot.gradle.plugin:3.5.6")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
}