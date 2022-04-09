package com.example.imagepicker

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagepicker.UI.ImageSearchAdapter
import com.example.imagepicker.UI.ViewModel
import com.example.imagepicker.UI.ViewModelFactory
import com.example.imagepicker.data.repository.Repository
import com.example.imagepicker.databinding.ImagePickActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageSearchActivity : AppCompatActivity() {
    private lateinit var actContext  : Context
    private lateinit var binding : ImagePickActivityBinding
    private lateinit var factory : ViewModelFactory
    private lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actContext  = this
        binding = ImagePickActivityBinding.inflate(layoutInflater)
        setUpViewModel()
        getSearchedName()
        setContentView(binding.root)
    }

    private fun getSearchedName(){
        binding.confirmButton.setOnClickListener {
            val searchedName = binding.editText.text.toString()
            if (searchedName.isNotEmpty()){
                setUpRecyclerView(searchedName)
                binding.editText.text?.clear()
            }else{
                Toast.makeText(this,"Please write searched tag",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setUpViewModel() {
        factory = ViewModelFactory (Repository(this))
        viewModel = ViewModelProvider(this,factory)[ViewModel::class.java]
    }

    private fun setUpRecyclerView(searchedName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val listOfImagesFromPixabay = viewModel.getImageByNameFromPixabay(searchedName)
            val listOfImagesFromPexels = viewModel.getImageByNameFromPexels(searchedName)
            val listOfImages = listOfImagesFromPexels + listOfImagesFromPixabay
            withContext(Dispatchers.Main) {
                binding.recyclerViewImagePick.layoutManager = LinearLayoutManager(actContext)
                val recyclerAdapter = ImageSearchAdapter(actContext, listOfImages)
                binding.recyclerViewImagePick.adapter = recyclerAdapter
            }
        }
    }
}

