package com.example.studyapp

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Created by wxk on 2020/5/5.
 */

class ProxyOnClick(var target: Any, var method: Method) : InvocationHandler {

    override fun invoke(proxy: Any?, _method: Method?, args: Array<out Any>): Any? {
        return method.invoke(target, *args)
    }
}