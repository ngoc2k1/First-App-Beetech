package com.example.firstapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapp.databinding.ItemChatGalleryBinding
import com.example.firstapp.featurechat.photo.Photo

class PhotoAdapter(private var context: Context, private var photoList: List<Photo>) :
    RecyclerView.Adapter<PhotoAdapter.ChatVH>() {
    var onClickListener: OnPhotoAdapterListener? = null

    class ChatVH(
        val binding: ItemChatGalleryBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatVH {
        return ChatVH(
            ItemChatGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ChatVH, position: Int) {

        var mPhoto = photoList[position]

        Glide.with(context)
            .load(mPhoto.uri)
            .into(holder.binding.ivItemPicture)

        holder.binding.cbItemClicked.setOnClickListener {
            onClickListener?.pickPhoto(mPhoto)
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}

interface OnPhotoAdapterListener {
    fun pickPhoto(photo: Photo)
}