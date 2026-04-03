package com.foodday.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodday.app.data.model.Product
import com.foodday.app.databinding.ItemProductBinding
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onAddToCart: ((Product) -> Unit)? = null
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {
    
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onProductClick(getItem(position))
                }
            }
            
            binding.btnAddToCart.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onAddToCart?.invoke(getItem(position))
                }
            }
        }
        
        fun bind(product: Product) {
            binding.apply {
                tvProductName.text = product.name
                tvRestaurantName.text = product.restaurantName ?: ""
                
                // Price
                tvPrice.text = currencyFormat.format(product.displayPrice)
                
                // Original price (if on sale)
                if (product.hasDiscount) {
                    tvOriginalPrice.visibility = View.VISIBLE
                    tvDiscountBadge.visibility = View.VISIBLE
                    tvOriginalPrice.text = currencyFormat.format(product.price)
                    tvOriginalPrice.paintFlags = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                    tvDiscountBadge.text = "-${product.discountPercentage}%"
                } else {
                    tvOriginalPrice.visibility = View.GONE
                    tvDiscountBadge.visibility = View.GONE
                }
                
                // Rating
                if (product.rating > 0) {
                    tvRating.visibility = View.VISIBLE
                    tvRating.text = "%.1f".format(product.rating)
                } else {
                    tvRating.visibility = View.GONE
                }
                
                // Delivery time
                if (!product.restaurantDeliveryTime.isNullOrEmpty()) {
                    tvDeliveryTime.visibility = View.VISIBLE
                    tvDeliveryTime.text = product.restaurantDeliveryTime
                } else {
                    tvDeliveryTime.visibility = View.GONE
                }
                
                // Image
                Glide.with(ivProduct)
                    .load(product.imageUrl)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(ivProduct)
                
                // Add to cart button
                btnAddToCart.visibility = if (onAddToCart != null) View.VISIBLE else View.GONE
            }
        }
    }
    
    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
