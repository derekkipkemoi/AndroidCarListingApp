package org.carlistingapp.listingcarsproject.ui.home.listCar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_image.view.*
import org.carlistingapp.listingcarsproject.GlideApp
import org.carlistingapp.listingcarsproject.R

class ViewImageAdapter(private val imageList : List<String>, private val context: Context) : RecyclerView.Adapter<ViewImageAdapter.ViewImageViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewImageViewHolder {
        //val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewImageViewHolder, position: Int) {
        val image = imageList[position]
        GlideApp.with(context).load(image).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class ViewImageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imageView = itemView.item_image!!
    }

}