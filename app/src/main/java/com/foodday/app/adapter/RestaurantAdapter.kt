package com.foodday.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodday.app.data.model.Restaurant
import com.foodday.app.databinding.ItemRestaurantBinding
import java.text.NumberFormat
import java.util.Locale

class RestaurantAdapter(
    private val onRestaurantClick: (Restaurant) -> Unit
) : ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(RestaurantDiffCallback()) {
    
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val binding = ItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RestaurantViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class RestaurantViewHolder(
        private val binding: ItemRestaurantBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onRestaurantClick(getItem(position))
                }
            }
        }
        
        fun bind(restaurant: Restaurant) {
            binding.apply {
                tvRestaurantName.text = restaurant.name
                
                // Rating
                if (restaurant.rating > 0) {
                    tvRating.visibility = View.VISIBLE
                    tvRating.text = "%.1f".format(restaurant.rating)
                } else {
                    tvRating.visibility = View.GONE
                }
                
                // Delivery time
                if (!restaurant.deliveryTime.isNullOrEmpty()) {
                    tvDeliveryTime.visibility = View.VISIBLE
                    tvDeliveryTime.text = restaurant.deliveryTime
                } else {
                    tvDeliveryTime.visibility = View.GONE
                }
                
                // Delivery fee
                if (restaurant.deliveryFee > 0) {
                    tvDeliveryFee.text = currencyFormat.format(restaurant.deliveryFee)
                } else {
                    tvDeliveryFee.text = "Free"
                }
                
                // Min order
                if (restaurant.minOrder > 0) {
                    tvMinOrder.visibility = View.VISIBLE
                    tvMinOrder.text = "Min ${currencyFormat.format(restaurant.minOrder)}"
                } else {
                    tvMinOrder.visibility = View.GONE
                }
                
                // Featured badge
                tvFeatured.visibility = if (restaurant.isFeatured) View.VISIBLE else View.GONE
                
                // Image
                Glide.with(ivRestaurant)
                    .load(restaurant.imageUrl)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(ivRestaurant)
            }
        }
    }
    
    class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
            return oldItem == newItem
        }
    }
}
