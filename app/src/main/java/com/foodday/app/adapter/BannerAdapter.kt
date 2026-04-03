package com.foodday.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foodday.app.data.model.Banner
import com.foodday.app.databinding.ItemBannerBinding

class BannerAdapter(
    private val onBannerClick: (Banner) -> Unit
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    
    private var banners: List<Banner> = emptyList()
    
    fun submitList(newBanners: List<Banner>) {
        banners = newBanners
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BannerViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(banners[position])
    }
    
    override fun getItemCount(): Int = banners.size
    
    inner class BannerViewHolder(
        private val binding: ItemBannerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onBannerClick(banners[position])
                }
            }
        }
        
        fun bind(banner: Banner) {
            Glide.with(binding.ivBanner)
                .load(banner.imageUrl)
                .centerCrop()
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(binding.ivBanner)
        }
    }
}
