package org.carlistingapp.listingcarsproject.ui.home.profile.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.item_car.view.*
import kotlinx.android.synthetic.main.item_car.view.car_textView_location
import kotlinx.android.synthetic.main.item_car.view.car_textView_name
import kotlinx.android.synthetic.main.item_car.view.car_textView_price
import kotlinx.android.synthetic.main.item_car.view.car_textView_price_negotiable
import kotlinx.android.synthetic.main.item_car.view.imageView
import kotlinx.android.synthetic.main.item_user_car.view.*
import org.carlistingapp.listingcarsproject.GlideApp
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import java.text.NumberFormat
import java.util.*

class UserCarsAdapter(private val userCars : List<CarObject>,private val context: Context, private val listener : OnItemClickListener
) : RecyclerView.Adapter<UserCarsAdapter.UserCarsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCarsViewHolder {
        return UserCarsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_car,parent,false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserCarsViewHolder, position: Int) {
        holder.carName.text = userCars[position].name
        holder.carPrice.text = "Ksh: "+ NumberFormat.getNumberInstance(Locale.US).format(userCars[position].price).toString()
        holder.carLocation.text = userCars[position].location
        if (userCars[position].priceNegotiable == true){
            holder.carPriceNegotiable.text = "Negotiable"
        }
        val carImage = userCars[position].images?.get(0)

        GlideApp.with(context).asBitmap().load(carImage).into(holder.carImage)
    }

    override fun getItemCount(): Int {
        return userCars.size
    }

    inner class UserCarsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var carName  = itemView.car_textView_name!!
        var  carPrice = itemView.car_textView_price!!
        var carImage = itemView.imageView!!
        var carLocation = itemView.car_textView_location!!
        var carPriceNegotiable = itemView.car_textView_price_negotiable!!
        private var editUserCarButton = itemView.button_edit
        private var deleteUserCarButton = itemView.button_delete
        private var featureUserCarButton = itemView.button_feature

        init {
            itemView.setOnClickListener(this)
            editUserCarButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener.onClickEditUserCar(position)
                }
            }

            featureUserCarButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener.onFeatureUserCar(position)
                }
            }

            deleteUserCarButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener.onClickDeleteUserCar(position)
                }
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun onClickEditUserCar(position: Int)
        fun onClickDeleteUserCar(position: Int)
        fun onFeatureUserCar(position: Int)
    }


}