package com.amadydev.drawingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amadydev.drawingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set the brush size
        binding.drawingView.setSizeForBrush(20.toFloat())

    }
}