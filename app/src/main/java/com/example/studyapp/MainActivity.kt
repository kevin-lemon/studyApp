package com.example.studyapp

import android.graphics.Color
import android.graphics.Typeface
import android.net.TrafficStats
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InjectUtils.injectOnClick(this)
//        btn1.text = sp
    }

    override fun onRestart() {
        super.onRestart()
    }

//    @OnClick([R.id.btn1, R.id.btn2, R.id.btn3])
//    fun onViewClick(view: View) {
//        when (view.id) {
//            R.id.btn1 -> {
//                Toast.makeText(this, "btn1", Toast.LENGTH_LONG).show()
//            }
//            R.id.btn2 -> {
//                Toast.makeText(this, "btn2", Toast.LENGTH_LONG).show()
//            }
//            R.id.btn3 -> {
//                Toast.makeText(this, "btn3", Toast.LENGTH_LONG).show()
//            }
//        }
//    }

}
