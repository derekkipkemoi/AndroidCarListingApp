package org.carlistingapp.listingcarsproject.ui.home.listCar.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import kotlinx.android.synthetic.main.item_car.view.*
import org.carlistingapp.listingcarsproject.GlideApp
import org.carlistingapp.listingcarsproject.R
import org.carlistingapp.listingcarsproject.data.db.entities.CarObject
import org.carlistingapp.listingcarsproject.ui.home.listCar.views.UnifiedNativeAdViewHolder
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

private const val CAR_VIEW_TYPE = 0
private const val UNIFIED_NATIVE_AD_VIEW_TYPE = 1

class ListForeignUsedCarAdapter(
    private val mRecyclerViewItems: ArrayList<Any>,
    private val context: Context,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                val unifiedNativeLayoutView = LayoutInflater.from(viewGroup.context).inflate(R.layout.ad_unified, viewGroup, false)
                UnifiedNativeAdViewHolder(unifiedNativeLayoutView)
            }
            else -> {
                val carViewHolder: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_car,viewGroup, false)
                CarViewHolder(carViewHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val recyclerViewItem: Any = mRecyclerViewItems[position]
        return if (recyclerViewItem is UnifiedNativeAd) {
            UNIFIED_NATIVE_AD_VIEW_TYPE
        } else CAR_VIEW_TYPE
    }

    override fun getItemCount(): Int {
       return mRecyclerViewItems.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            UNIFIED_NATIVE_AD_VIEW_TYPE -> {
                populateNativeAdView(
                    mRecyclerViewItems[position] as UnifiedNativeAd, (holder as UnifiedNativeAdViewHolder).adView
                )
            }

            CAR_VIEW_TYPE -> {
                val carViewHolder = holder as CarViewHolder
                val carObject = mRecyclerViewItems[position] as CarObject

                carViewHolder.carName.text = carObject.name
                carViewHolder.carPrice.text = "Ksh: "+NumberFormat.getNumberInstance(Locale.US).format(carObject.price).toString()
                carViewHolder.carLocation.text = carObject.location
                if (carObject.priceNegotiable == true){
                    carViewHolder.carPriceNegotiable.text = "Negotiable"
                }
                val carImage = carObject.images?.get(0)
                GlideApp.with(context).asBitmap().load(carImage).into(carViewHolder.carImage)

            }
        }
    }



    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
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
                   listener.shareButtonClick(position)
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
        fun shareButtonClick(position: Int)
    }


    private fun populateNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        (adView.headlineView as TextView).text = nativeAd.headline
        (adView.bodyView as TextView).text = nativeAd.body
        (adView.callToActionView as Button).text = nativeAd.callToAction

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        val icon = nativeAd.icon
        if (icon == null) {
            adView.iconView.visibility = View.INVISIBLE
        } else {
            (adView.iconView as ImageView).setImageDrawable(icon.drawable)
            adView.iconView.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            adView.priceView.visibility = View.INVISIBLE
        } else {
            adView.priceView.visibility = View.VISIBLE
            (adView.priceView as TextView).text = nativeAd.price
        }
        if (nativeAd.store == null) {
            adView.storeView.visibility = View.INVISIBLE
        } else {
            adView.storeView.visibility = View.VISIBLE
            (adView.storeView as TextView).text = nativeAd.store
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar).rating = nativeAd.starRating.toFloat()
            adView.starRatingView.setVisibility(View.VISIBLE)
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView).text = nativeAd.advertiser
            adView.advertiserView.setVisibility(View.VISIBLE)
        }

        adView.setNativeAd(nativeAd)
    }

}