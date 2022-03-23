package com.example.imagepicker.UI

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.imagepicker.R
import com.example.imagepicker.data.RoomDatabase.ImageDB
import com.squareup.picasso.Picasso


class UsersImagesAdapter(
    private val actContext : Context,
    private var listOfPhotos : List<ImageDB>
) : RecyclerView.Adapter<UsersImagesAdapter.ViewHolder> () {
    var onClickDeleteItem :((link:String)->Unit)? = null
    var onClickDownloadItem : ((link:String) -> Unit)? = null
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var photo : ImageView= view.findViewById(R.id.image)
        var deleteButton : ImageButton = view.findViewById(R.id.deleteButton)
        var downloadButton : ImageButton = view.findViewById(R.id.downloadButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(listOfPhotos[position].imageLink).into(holder.photo)
        holder.deleteButton.setOnClickListener {
            onClickDeleteItem?.invoke(listOfPhotos[position].imageLink)
        }

        holder.downloadButton.setOnClickListener {
            onClickDownloadItem?.invoke(listOfPhotos[position].imageLink)
        }

    }

    @JvmName("setOnClickDeleteItem1")
    fun setOnClickDeleteItem(callback:(link:String)-> Unit){
        this.onClickDeleteItem = callback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListOfImages(array1: List<ImageDB>){
        this.listOfPhotos = array1
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listOfPhotos.size
    }

    fun setOnDownloadItem (callback:(link:String) -> Unit){
        this.onClickDownloadItem = callback
    }


}