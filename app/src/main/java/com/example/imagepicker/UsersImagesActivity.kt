package com.example.imagepicker


import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagepicker.UI.UsersImagesAdapter
import com.example.imagepicker.UI.ViewModel
import com.example.imagepicker.UI.ViewModelFactory
import com.example.imagepicker.data.repository.Repository
import com.example.imagepicker.databinding.UsersImageActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL
import java.util.*


class UsersImagesActivity : AppCompatActivity() {
    private lateinit var recyclerAdapter: UsersImagesAdapter
    private lateinit var actContext: Context
    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: ViewModel
    private lateinit var binding: UsersImageActivityBinding

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UsersImageActivityBinding.inflate(layoutInflater)
        actContext = this
        setUpViewModel()
        setUpRecyclerView()
        setContentView(binding.root)
        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            readPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
            writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: writePermissionGranted


        }
    }

    private fun setUpViewModel() {
        factory = ViewModelFactory(Repository(this))
        viewModel = ViewModelProvider(this, factory)[ViewModel::class.java]
    }


    private fun setUpRecyclerView() {
        viewModel.getFavImages().observe(this) { it ->
            binding.recyclerViewUsersImages.layoutManager = LinearLayoutManager(actContext)
            recyclerAdapter = UsersImagesAdapter(actContext, it)
            binding.recyclerViewUsersImages.adapter = recyclerAdapter
            recyclerAdapter.setOnClickDeleteItem {
                setDeleteDialog(it)
            }

            recyclerAdapter.setOnDownloadItem {
                setDownloadDialog(it)
            }

        }
    }

    private fun setDeleteDialog(link: String) {
        val builder = AlertDialog.Builder(actContext)
        builder.setMessage("Do you want to delete photo?")
        builder.setCancelable(true)
        builder.setPositiveButton("yes") { _, _ ->
            Toast.makeText(actContext, "Deleted photo", Toast.LENGTH_LONG).show()
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteByLinkFromDb(link)
            }
            viewModel.getFavImages().observe(this) {
                recyclerAdapter.setListOfImages(it)
            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun setDownloadDialog(link: String) {
        val builder = AlertDialog.Builder(actContext)
        builder.setMessage("Do you want to download photo?")
        builder.setCancelable(true)
        builder.setPositiveButton("yes") { dialog, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                val bitmap = (getBitmapFromUrl(link))

                updateOrRequestPermissions()
                if (savePhotoToExternalStorage(UUID.randomUUID().toString(), bitmap!!, contentResolver)) {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(actContext, "Photo downloaded", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }
    private fun updateOrRequestPermissions() {
        val readPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val writePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    val min29Sdk = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        readPermissionGranted = readPermission
        writePermissionGranted = writePermission

        val requestList= mutableListOf<String>()

        if (!readPermission) {
            requestList.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if(!writePermission || min29Sdk) {
            requestList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if(requestList.isNotEmpty()){
            permissionsLauncher.launch(requestList.toTypedArray())
        }
}


}

