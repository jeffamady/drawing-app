package com.amadydev.drawingapp

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
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
import androidx.core.view.marginLeft
import com.amadydev.drawingapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mImageButtonCurrentPaint: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showSnackbar(binding.root.rootView, "Welcome to the app", Color.GREEN)

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
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
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
            create()
            setCancelable(false)
            show()
        }
    }

    // Snack Bar
    private fun showSnackbar(view: View, text: String, color: Int) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).apply {
            this.view.setBackgroundColor(color)
            this.setTextColor(Color.BLACK)
            this.view.setPadding(0, 0, 0, 0)
        }.run { show() }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
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


    // Permissions
    private val cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

            permissions.entries.forEach { permision ->
                val permissionName = permision.key
                val isGranted = permision.value
                if (isGranted) {
                    when (permissionName) {
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            showToast("Yes is granted for fine location")
                        }
                        Manifest.permission.ACCESS_COARSE_LOCATION -> {
                            showToast("Yes is granted for coarse location")

                        }
                        else -> {
                            showToast("Yes is granted for camera")
                        }
                    }
                } else {
                    when (permissionName) {
                        Manifest.permission.ACCESS_FINE_LOCATION -> {
                            showToast("Permission denied for fine location")
                        }
                        Manifest.permission.ACCESS_COARSE_LOCATION -> {
                            showToast("Permission denied for coarse location")
                        }
                        else -> {
                            showToast("Permission denied for camera")
                        }
                    }
                }
            }
        }




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
}