package com.ste.lottoactivity

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    abstract fun setUpEvents()
    abstract fun setValues()

}