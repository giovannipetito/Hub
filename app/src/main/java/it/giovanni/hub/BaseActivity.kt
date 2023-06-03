package it.giovanni.hub

import androidx.activity.ComponentActivity

abstract class BaseActivity : ComponentActivity() {

    open fun log(tag: String, message: String) {}
}