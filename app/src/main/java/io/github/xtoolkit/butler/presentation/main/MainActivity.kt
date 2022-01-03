package io.github.xtoolkit.butler.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.xtoolkit.butler.shopping.presentation.main.MainActivity as ShoppingMainActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, ShoppingMainActivity::class.java))
        finish()
    }
}
