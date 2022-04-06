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
import com.example.imagepicker.data.Retrofit.Hit
import com.example.imagepicker.data.repository.Repository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var newListOfPhotos : List<String>
class ImageSearchAdapter(
    private val actContext  : Context,
    private val listOfPhotos : List<Hit>
) : RecyclerView.Adapter<ImageSearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_search_item, parent,false)
        newListOfPhotos = deleteRepeatedItems(listOfPhotos)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var doubledPosition : Int
        if(position ==0){
            doubledPosition = 0
        }
        doubledPosition = position*2
        if(doubledPosition < newListOfPhotos.size-1) {
            Picasso.get().load(newListOfPhotos[doubledPosition]).into(holder.photo1)
            Picasso.get().load(newListOfPhotos[doubledPosition + 1]).into(holder.photo2)


            holder.photo1.setOnClickListener {
                setDialog(newListOfPhotos[doubledPosition])
            }
            holder.photo2.setOnClickListener {
                setDialog(newListOfPhotos[doubledPosition + 1])
            }
        }
    }

    private fun setDialog(link : String) {
        val repository = Repository(actContext )
        val builder =AlertDialog.Builder(actContext )
        builder.setMessage("Do you want to add photo?")
        builder.setCancelable(true)
        builder.setPositiveButton("yes"){dialog,_ ->
            CoroutineScope(Dispatchers.IO).launch {
                repository.insertLinkToDb(link)
            }
            Toast.makeText(actContext ,"Added photo", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No"){dialog,_->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    override fun getItemCount(): Int {
        return listOfPhotos.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var photo1 : ImageView = view.findViewById(R.id.imageView1)
        var photo2 : ImageView = view.findViewById(R.id.imageView2)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun deleteRepeatedItems(listOfPhotos: List<Hit>) : List<String> {
        val listOfNewPhotos = mutableListOf<String>()
        for (i in listOfPhotos){
            if (!listOfNewPhotos.contains(i.webformatURL)){
                listOfNewPhotos.add(i.webformatURL)
            }
        }
        return listOfNewPhotos
    }

}