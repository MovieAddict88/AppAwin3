package com.foodday.app.ui.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foodday.app.R
import com.foodday.app.databinding.ItemOnboardingBinding

class OnboardingAdapter(
    private val items: List<OnboardingItem>
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OnboardingViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(items[position])
    }
    
    override fun getItemCount(): Int = items.size
    
    inner class OnboardingViewHolder(
        private val binding: ItemOnboardingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(item: OnboardingItem) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.description
            
            // Set illustration based on position
            val imageRes = when (adapterPosition) {
                0 -> R.drawable.ic_onboarding_food
                1 -> R.drawable.ic_onboarding_order
                2 -> R.drawable.ic_onboarding_delivery
                3 -> R.drawable.ic_onboarding_tracking
                else -> R.drawable.ic_onboarding_food
            }
            binding.ivIllustration.setImageResource(imageRes)
        }
    }
}
