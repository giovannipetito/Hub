package it.giovanni.hub

import androidx.activity.ComponentActivity

abstract class BaseActivity : ComponentActivity() {

    open fun hubLog(tag: String, message: String) {}
}