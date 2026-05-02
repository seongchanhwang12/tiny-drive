import gradle.kotlin.dsl.accessors._2c95f20277cbe6143532f6e8d67e36cc.annotationProcessor
import gradle.kotlin.dsl.accessors._2c95f20277cbe6143532f6e8d67e36cc.compileOnly

plugins{
    id("drive.java-common")
}

dependencies{
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}