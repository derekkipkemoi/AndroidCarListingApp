package org.carlistingapp.listingcarsproject.ui.home.listCar.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_car.view.*
import org.carlistingapp.listingcarsproject.GlideApp
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import java.text.NumberFormat
import java.util.*

class FilterCarsAdapter(private val cars : List<CarObject>, private  val context: Context, private val listener: OnItemClickListener)
    : RecyclerView.Adapter<FilterCarsAdapter.FilterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.carName.text = cars[position].name
        holder.carPrice.text = "Ksh: "+ NumberFormat.getNumberInstance(Locale.US).format(cars[position].price).toString()
        holder.carLocation.text = cars[position].location
        if (cars[position].priceNegotiable == true){
            holder.carPriceNegotiable.text = "Negotiable"
        }
        val carImageUrl = cars[position].images?.get(0)
        GlideApp.with(context).asBitmap().load(carImageUrl).into(holder.carImage)
    }

    override fun getItemCount(): Int {
        return  cars.size
    }

    inner class FilterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var carName  = itemView.car_textView_name!!
        var  carPrice = itemView.car_textView_price!!
        var carImage = itemView.imageView!!
        var carLocation = itemView.car_textView_location!!
        var carPriceNegotiable = itemView.car_textView_price_negotiable!!
        private val contactButton = itemView.button_contact!!
        private val shareButton = itemView.button_share

        init {
            itemView.setOnClickListener(this)
            contactButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener.contactSellerOnClick(position)
                }
            }
            shareButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener.shareButtonOnClick(position)
                }
            }
        }


        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun contactSellerOnClick(position: Int)
        fun shareButtonOnClick(position: Int)
    }
}