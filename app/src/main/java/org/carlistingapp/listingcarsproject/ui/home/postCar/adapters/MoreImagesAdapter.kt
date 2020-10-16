package org.carlistingapp.listingcarsproject.ui.home.postCar.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_more_images.view.*
import org.carlistingapp.listingcarsproject.R

class MoreImagesAdapter(private val images : ArrayList<Uri>, private val context: Context) : RecyclerView.Adapter<MoreImagesAdapter.MoreImagesViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreImagesViewHolder {
        return MoreImagesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_more_images, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MoreImagesViewHolder, position: Int) {
        val image = images[position]
        Glide.with(context).load(image).into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return images.size
    }

    class MoreImagesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var imageView = itemView.imageView!!
    }

}