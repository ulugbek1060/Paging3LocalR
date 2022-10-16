package com.android.paging3test.model.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.paging3test.R
import com.android.paging3test.databinding.ItemUserBinding
import com.android.paging3test.model.User
import com.bumptech.glide.Glide

/**
 * Adapter for rendering users list in a RecyclerView.
 */
class UsersAdapter : PagingDataAdapter<User, UsersAdapter.Holder>(UsersDiffCallback()) {

   override fun onBindViewHolder(holder: Holder, position: Int) {
      val user = getItem(position) ?: return
      with(holder.binding) {
         userNameTextView.text = user.name
         userCompanyTextView.text = user.company
         loadUserPhoto(photoImageView, user.imageUrl)
      }
   }

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
      val inflater = LayoutInflater.from(parent.context)
      val binding = ItemUserBinding.inflate(inflater, parent, false)
      return Holder(binding)
   }

   private fun loadUserPhoto(imageView: ImageView, url: String) {
      val context = imageView.context
      if (url.isNotBlank()) {
         Glide.with(context)
            .load(url)
            .circleCrop()
            .placeholder(R.drawable.ic_user_avatar)
            .error(R.drawable.ic_user_avatar)
            .into(imageView)
      } else {
         Glide.with(context)
            .load(R.drawable.ic_user_avatar)
            .into(imageView)
      }
   }

   class Holder(
      val binding: ItemUserBinding
   ) : RecyclerView.ViewHolder(binding.root)

}

// ---

class UsersDiffCallback : DiffUtil.ItemCallback<User>() {
   override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
      return oldItem.id == newItem.id
   }

   override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
      return oldItem == newItem
   }
}