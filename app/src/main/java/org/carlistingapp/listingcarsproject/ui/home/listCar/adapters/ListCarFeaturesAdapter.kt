package org.carlistingapp.listingcarsproject.ui.home.listCar.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_feature.view.*
import org.carlistingapp.listingcarsproject.R

class ListCarFeaturesAdapter(private val features : ArrayList<String>, private val context: Context) : RecyclerView.Adapter<ListCarFeaturesAdapter.FeaturesViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturesViewHolder {
        return FeaturesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_feature, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FeaturesViewHolder, position: Int) {
        holder.feature.text = features[position]
    }

    override fun getItemCount(): Int {
       return features.size
    }


    class FeaturesViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var feature = itemView.car_feature
    }



}