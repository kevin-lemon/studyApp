package com.example.studyapp

import kotlin.reflect.KClass

/**
 * Created by wxk on 2020/5/8.
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventType(val listenerType: KClass<*>,val listenerSetter: String
)