package com.amadydev.drawingapp

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import androidx.core.view.get
import com.amadydev.drawingapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mImageButtonCurrentPaint: ImageButton

    // Camera Permission
    private val cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(
                    this,
                    "Yes is granted for camera",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                Toast.makeText(
                    this,
                    "Permission denied for camera ",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    // Camera Permission
    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            it.entries.forEach { permision ->
                val permissionName = permision.key
                val isGranted = permision.value
                if (isGranted) {
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(
                            this,
                            "Yes is granted for fine location",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (permissionName == Manifest.permission.ACCESS_COARSE_LOCATION) {
                        Toast.makeText(
                            this,
                            "Yes is granted for coarse location",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }else {
                        Toast.makeText(
                            this,
                            "Yes is granted for camera",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }
                } else {
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(
                            this,
                            "Permission denied for fine location",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else if (permissionName == Manifest.permission.ACCESS_COARSE_LOCATION) {
                        Toast.makeText(
                            this,
                            "Permission denied for coarse location",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    } else {
                        Toast.makeText(
                            this,
                            "Permission denied for camera",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }

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

        ibGallery.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            ) {
                showRationaleDialog(
                    "${getString(R.string.app_name)} requires camera access",
                    "Camera cannot be used because Camera is denied, go to the setting to allow it"
                )
            } else {
                cameraAndLocationResultLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        }
    }

    private fun showRationaleDialog(
        title: String,
        message: String
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }.run { create().show() }
    }

    private fun showBrushChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)

        with(binding) {
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
        if (view != mImageButtonCurrentPaint) {
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            binding.drawingView.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this@MainActivity, R.drawable.pallet_selected)
            )

            mImageButtonCurrentPaint.setImageDrawable(
                ContextCompat.getDrawable(this@MainActivity, R.drawable.pallet_normal)
            )

            mImageButtonCurrentPaint = view
        }
    }
}