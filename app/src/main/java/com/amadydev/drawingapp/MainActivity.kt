package com.amadydev.drawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.core.view.size
import com.amadydev.drawingapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mImageButtonCurrentPaint: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            //Set the brush size
            drawingView.setSizeForBrush(20.toFloat())

            mImageButtonCurrentPaint = llPaintColors[1] as ImageButton


            //Listeners
            setListeners()
        }

    }

    private fun ActivityMainBinding.setListeners() {
        ibBrush.setOnClickListener {
            showBrushChooserDialog()
        }

        llPaintColors.forEach { imageButton ->
            imageButton.setOnClickListener {
                paintClicked(it)
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
    
    private fun paintClicked(view: View) {
        if(view != mImageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            binding.drawingView.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this@MainActivity, R.drawable.pallet_selected )
            )

            mImageButtonCurrentPaint.setImageDrawable(
                ContextCompat.getDrawable(this@MainActivity, R.drawable.pallet_normal )
            )

            mImageButtonCurrentPaint = view
        }
    }
}