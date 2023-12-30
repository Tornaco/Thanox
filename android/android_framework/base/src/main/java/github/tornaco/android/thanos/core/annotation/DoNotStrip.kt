package github.tornaco.android.thanos.core.annotation

import android.annotation.SuppressLint
import java.lang.annotation.ElementType

@Suppress("DEPRECATED_JAVA_ANNOTATION")
@SuppressLint("SupportAnnotationUsage")
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FILE,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD
)
// Needed due to Kotlin's lack of PACKAGE annotation target
// https://youtrack.jetbrains.com/issue/KT-45921
@java.lang.annotation.Target(
    ElementType.PACKAGE,
    ElementType.TYPE,
    ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR,
    ElementType.METHOD,
    ElementType.FIELD
)
annotation class DoNotStrip