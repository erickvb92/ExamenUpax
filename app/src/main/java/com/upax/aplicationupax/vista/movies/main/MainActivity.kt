package com.upax.aplicationupax.vista.movies.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.upax.aplicationupax.R
import com.upax.aplicationupax.vista.movies.menu.MenuActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        iniciar.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
    }
}