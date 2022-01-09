package com.amadydev.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.amadydev.drawingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            //Set the brush size
            drawingView.setSizeForBrush(20.toFloat())

            //Listeners
            ibBrush.setOnClickListener {
                showBrushChooserDialog()
            }
        }

    }

    private fun showBrushChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)

        with(binding){
            val smallBtn: ImageButton = brushDialog.findViewById(R.id.ib_small_brush)
            smallBtn.setOnClickListener {
                drawingView.setSizeForBrush(10.toFloat())
                brushDialog.dismiss()
            }

            val mediumBtn: ImageButton = brushDialog.findViewById(R.id.ib_medium_brush)
            mediumBtn.setOnClickListener {
                drawingView.setSizeForBrush(15.toFloat())
                brushDialog.dismiss()
            }

            val largeBtn: ImageButton = brushDialog.findViewById(R.id.ib_large_brush)
            largeBtn.setOnClickListener {
                drawingView.setSizeForBrush(20.toFloat())
                brushDialog.dismiss()
            }
        }
        brushDialog.show()
    }
}