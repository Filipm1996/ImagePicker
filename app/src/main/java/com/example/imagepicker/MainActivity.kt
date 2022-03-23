package com.example.imagepicker

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.imagepicker.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var photoUri : Uri
    lateinit var currentPhotoPath: String
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setUpClickListeners()
        setContentView(binding.root)
    }

    private fun setUpClickListeners() {
        binding.ImageSearchButton.setOnClickListener {
            val intent = Intent(this, ImageSearchActivity::class.java)
            startActivity(intent)
        }

        binding.myImagesButton.setOnClickListener {
            val intent = Intent(this, UsersImagesActivity::class.java)
            startActivity(intent)
        }

        binding.takePictureButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(this,"Photo taken", Toast.LENGTH_LONG).show()

            val bitmap = if (Build.VERSION.SDK_INT >= 29)
            {
                val source = ImageDecoder.createSource(contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            }
                else
                {
                    MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
                }
            savePhotoToExternalStorage(UUID.randomUUID().toString(), bitmap , contentResolver)
        }
    }


    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = this.absolutePath
        }
    }



    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    photoUri = FileProvider.getUriForFile(
                        this,
                        "com.mydomain.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    resultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

}
