package com.example.firstapp.featurechat

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.databinding.ItemChatMultipictureBinding


class PhotoAdapterv2(private var context: Context, private val photoList: List<Uri>) :
    RecyclerView.Adapter<PhotoAdapterv2.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatMultipictureBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindingItemMultiPhoto.imageViewPhoto.setImageURI(photoList[position])

    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class ViewHolder(
        val bindingItemMultiPhoto: ItemChatMultipictureBinding
    ) : RecyclerView.ViewHolder(bindingItemMultiPhoto.root)

}