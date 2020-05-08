package com.example.studyapp

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass


/**
 * Created by wxk on 2020/5/5.
 */
object InjectUtils {

    fun injectOnClick(activity: AppCompatActivity) {
        val clazz = activity.javaClass
        val functions = clazz.declaredMethods
        functions?.forEach { function ->

            //获得方法上所有注解
            val annotations: Array<Annotation> = function.annotations
            annotations?.forEach { annotation ->
                //注解类型
                val annotationType: Class<out Annotation?> =
                    annotation.annotationClass.java
                if (annotationType.isAnnotationPresent(EventType::class.java)) {
                    val eventAnnotation = annotationType.getAnnotation(EventType::class.java)
                    eventAnnotation?.let {
                        val listenerType = eventAnnotation.listenerType
                        val listenerSetter = eventAnnotation.listenerSetter
                        val method = annotationType.getDeclaredMethod("values")
                        val values: IntArray? = method.invoke(annotation) as IntArray
                        method.isAccessible = true
                        if (values?.isNotEmpty() == true) {
                            proxyOnClickListener(
                                activity,
                                function,
                                values,
                                listenerType,
                                listenerSetter
                            )
                        }
                    }
                }
            }
        }
    }

    private fun proxyOnClickListener(
        activity: AppCompatActivity,
        functions: Method,
        values: IntArray,
        listenerType: KClass<*>,
        listenerSetter: String
    ) {
        val listener = Proxy.newProxyInstance(
            listenerType.java.classLoader,
            arrayOf<Class<*>>(
                listenerType.java
            ),
            ProxyOnClick(activity, functions)
        )
        try {
            values.forEach {
                var view = activity.findViewById<View>(it)
                var targetMethod =
                    view.javaClass.getMethod(listenerSetter, listenerType.java)
                targetMethod.invoke(view, listener)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }
}