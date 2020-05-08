package com.example.studyapp

import android.view.View


/**
 * Created by wxk on 2020/5/5.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@EventType(View.OnClickListener::class, "setOnClickListener")
annotation class OnClick(val values:IntArray)