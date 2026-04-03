package com.foodday.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodday.app.data.model.CartItem
import com.foodday.app.databinding.ItemCartItemBinding
import java.text.NumberFormat
import java.util.Locale

class CartItemAdapter(
    private val onQuantityChange: (CartItem, Int) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : ListAdapter<CartItem, CartItemAdapter.CartItemViewHolder>(CartItemDiffCallback()) {
    
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = ItemCartItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartItemViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class CartItemViewHolder(
        private val binding: ItemCartItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(cartItem: CartItem) {
            binding.apply {
                tvProductName.text = cartItem.displayName ?: cartItem.productName
                tvPrice.text = currencyFormat.format(cartItem.displayPrice)
                tvQuantity.text = cartItem.quantity.toString()
                tvSubtotal.text = currencyFormat.format(cartItem.subtotal)
                
                // Show variant name if available
                if (!cartItem.variantName.isNullOrEmpty()) {
                    tvVariantName.visibility = View.VISIBLE
                    tvVariantName.text = cartItem.variantName
                } else {
                    tvVariantName.visibility = View.GONE
                }
                
                Glide.with(ivProduct)
                    .load(cartItem.imageUrl)
                    .centerCrop()
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(ivProduct)
                
                btnDecrease.setOnClickListener {
                    if (cartItem.quantity > 1) {
                        onQuantityChange(cartItem, cartItem.quantity - 1)
                    }
                }
                
                btnIncrease.setOnClickListener {
                    onQuantityChange(cartItem, cartItem.quantity + 1)
                }
                
                btnRemove.setOnClickListener {
                    onRemove(cartItem)
                }
            }
        }
    }
    
    class CartItemDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }
}
