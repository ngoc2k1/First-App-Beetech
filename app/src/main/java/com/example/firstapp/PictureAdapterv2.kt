package com.example.firstapp

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.databinding.ItemChatMultipictureBinding
import com.example.firstapp.featurechat.Chat


class PictureAdapterv2(private var context: Context, private val pictureList: List<Uri>) :
    RecyclerView.Adapter<PictureAdapterv2.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatMultipictureBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindingItemMultiPicture.imageView.setImageURI(pictureList[position])

    }

    override fun getItemCount(): Int {
        return pictureList.size
    }

    class ViewHolder(
        val bindingItemMultiPicture: ItemChatMultipictureBinding
    ) : RecyclerView.ViewHolder(bindingItemMultiPicture.root)

}