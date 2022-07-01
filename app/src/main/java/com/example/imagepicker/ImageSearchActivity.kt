package com.example.imagepicker

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
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
    private lateinit var recyclerAdapter: ImageSearchAdapter
    private lateinit var actContext  : Context
    private lateinit var binding : ImagePickActivityBinding
    private lateinit var factory : ViewModelFactory
    private lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actContext  = this
        binding = ImagePickActivityBinding.inflate(layoutInflater)
        setUpViewModel()
        setUpErrorObserver()
        setUpLoadingObserver()
        getSearchedName()
        setContentView(binding.root)
    }

    private fun setUpLoadingObserver() {
        viewModel.getLoadingLiveData().observe(this){loading ->
            if(loading == true){
                binding.progressBar.visibility = View.VISIBLE
            } else{
                binding.progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun setUpErrorObserver() {
        viewModel.getErrors().observe(this){error ->
            if(!error.isNullOrEmpty()){
                Toast.makeText(this,error,Toast.LENGTH_SHORT).show()
                viewModel.clearErrorCollector()
            }
        }
    }

    private fun getSearchedName(){
        binding.confirmButton.setOnClickListener {
            val searchedName = binding.editText.text.toString()
            if (searchedName.isNotEmpty()){
                setUpRecyclerView(searchedName)
                binding.editText.text?.clear()
                hideKeyboard(this)
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
            viewModel.getImageByNameFromPixabay(searchedName)
            viewModel.getImageByNameFromPexels(searchedName)
            viewModel.getLinks().observe(this) {listOfImages->
                binding.recyclerViewImagePick.layoutManager = LinearLayoutManager(actContext)
                recyclerAdapter = ImageSearchAdapter()
                recyclerAdapter.updateListOfPhotos(listOfImages)
                setUpClickListener()
                binding.recyclerViewImagePick.adapter = recyclerAdapter
            }
        }


    private fun setUpClickListener() {
        recyclerAdapter.setOnAddPhotoClickListener {
            viewModel.setAddPhotoDialog(it, actContext)
        }
    }

    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
}

