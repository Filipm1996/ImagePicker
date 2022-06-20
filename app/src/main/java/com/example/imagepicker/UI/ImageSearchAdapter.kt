package com.example.imagepicker.UI

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.imagepicker.R
import com.example.imagepicker.data.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ImageSearchAdapter: RecyclerView.Adapter<ImageSearchAdapter.ViewHolder>() {
    private var listOfPhotos : List<String>? = null
    private var addPhotoClicked : ((String)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_search_item, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var doubledPosition : Int
        if(position ==0){
            doubledPosition = 0
        }
        doubledPosition = position*2
        if(doubledPosition < listOfPhotos!!.size-1) {
            Picasso.get().load(listOfPhotos!![doubledPosition]).into(holder.photo1)
            Picasso.get().load(listOfPhotos!![doubledPosition + 1]).into(holder.photo2)


            holder.photo1.setOnClickListener {
                addPhotoClicked?.invoke(listOfPhotos!![doubledPosition])
            }
            holder.photo2.setOnClickListener {
                addPhotoClicked?.invoke(listOfPhotos!![doubledPosition + 1])
            }
        }
    }
    fun setOnAddPhotoClickListener(callback : (String)->Unit){
        this.addPhotoClicked = callback
    }

    fun updateListOfPhotos(list : List<String>){
        this.listOfPhotos = list
    }

    override fun getItemCount(): Int {
        return listOfPhotos?.size?.div(2) ?: 0
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var photo1 : ImageView = view.findViewById(R.id.imageView1)
        var photo2 : ImageView = view.findViewById(R.id.imageView2)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}