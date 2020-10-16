package org.carlistingapp.listingcarsproject.ui.home.listCar.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.item_car_slider.view.*
import org.carlistingapp.listingcarsproject.GlideApp
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import java.text.NumberFormat
import java.util.*

class SliderAdapter(
    private val carSliderImages : ArrayList<CarObject>,
    private val viewPager2: ViewPager2, private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_car_slider, parent, false)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setCar(carSliderImages[position],context)
        if (position == carSliderImages.size - 2){
            viewPager2.post(runAble)
        }
    }

    override fun getItemCount(): Int {
        return carSliderImages.size
    }

    inner class SliderViewHolder(sliderItemView : View) : RecyclerView.ViewHolder(sliderItemView) {
        private var sliderImage = sliderItemView.imageView_slider
        private var carName = sliderItemView.car_textView_name_slider
        private var carLocation = sliderItemView.car_textView_location_slider
        private var carPrice = sliderItemView.car_textView_price_slider
        private var carPriceNegotiable = sliderItemView.car_textView_price_negotiable_slider

        @SuppressLint("SetTextI18n")
        fun setCar(carObject: CarObject, context: Context){
            val carImage = carObject.images?.get(0)
            GlideApp.with(context).asBitmap().load(carImage).into(sliderImage)
            carName.text = carObject.name
            carLocation.text = carObject.location
            carPrice.text = "Ksh: "+ NumberFormat.getNumberInstance(Locale.US).format(carObject.price).toString()
            if (carObject.priceNegotiable == true){
                carPriceNegotiable.text = "Negotiable"
            }
        }

        init {
            sliderImage.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position)
                }
            }
        }


    }

    private val runAble = Runnable {
        carSliderImages.addAll(carSliderImages)
        notifyDataSetChanged()
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}