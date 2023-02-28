package com.example.firstapp.featurechat

import android.content.Context
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapp.R
import com.example.firstapp.databinding.ItemChatOnepictureReceiveBinding
import com.example.firstapp.databinding.ItemChatOnepictureSendBinding
import com.example.firstapp.databinding.ItemChatReceiveBinding
import com.example.firstapp.databinding.ItemChatSendBinding

enum class ChatAction(val original: Int) {
    RECEIVE(0), SEND(1), RECEIVE_PHOTO(10), SEND_PHOTO(11)
}

class ChatAdapter(
    private var context: Context, var listMessageChat: ArrayList<Chat>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemChatReceiveVH(
        val bindingReceive: ItemChatReceiveBinding
    ) : RecyclerView.ViewHolder(bindingReceive.root)

    class ItemChatSendVH(
        val bindingSend: ItemChatSendBinding
    ) : RecyclerView.ViewHolder(bindingSend.root)

    class ItemChatOnepictureSendVH(
        val bindingSendPhoto: ItemChatOnepictureSendBinding
    ) : RecyclerView.ViewHolder(bindingSendPhoto.root)

    class ItemChatOnepictureReceiveVH(
        val bindingReceivePhoto: ItemChatOnepictureReceiveBinding
    ) : RecyclerView.ViewHolder(bindingReceivePhoto.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ChatAction.RECEIVE.original) return ItemChatReceiveVH(
            ItemChatReceiveBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
        if (viewType == ChatAction.SEND.original) return ItemChatSendVH(
            ItemChatSendBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
        if (viewType == ChatAction.RECEIVE_PHOTO.original)
            return ItemChatOnepictureReceiveVH(
                ItemChatOnepictureReceiveBinding.inflate(
                    LayoutInflater.from(context), parent, false
                )
            )
        return ItemChatOnepictureSendVH(
            ItemChatOnepictureSendBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ChatAction.RECEIVE.original -> {
                val userFeatureViewHolder = holder as ItemChatReceiveVH
                userFeatureViewHolder.bindingReceive.tvItemchatMess.text =
                    listMessageChat[position].message
            }
            ChatAction.SEND.original -> {
                val userFeatureViewHolder = holder as ItemChatSendVH
                userFeatureViewHolder.bindingSend.tvItemSendMess.text =
                    listMessageChat[position].message
            }
            ChatAction.RECEIVE_PHOTO.original -> {
//                val userFeatureViewHolder = holder as ItemChatOnepictureReceiveVH
//                Glide.with(context).load(R.drawable.avatar)
//                    .into(userFeatureViewHolder.bindingReceivePhoto.ivItemOnepicture)
            }
            ChatAction.SEND_PHOTO.original -> {
                val userFeatureViewHolder = holder as ItemChatOnepictureSendVH
                Glide.with(context)
                    .load(listMessageChat[position].uri)
                    .override(
                        (300 * holder.itemView.context.resources.displayMetrics.density).toInt(),
                        (300 * holder.itemView.context.resources.displayMetrics.density).toInt()
                    )
                    .into(userFeatureViewHolder.bindingSendPhoto.ivItemOnepicture)
            }
        }
    }


    override fun getItemCount(): Int {
        return listMessageChat.size
    }

    override fun getItemViewType(position: Int): Int {
        if (listMessageChat[position].isSend == 0) {
            return ChatAction.RECEIVE.original
        }
        if (listMessageChat[position].isSend == 1) {
            return ChatAction.SEND.original
        }
        if (listMessageChat[position].isSendPhoto == 10) {
            return ChatAction.RECEIVE_PHOTO.original
        }
        return ChatAction.SEND_PHOTO.original
    }
}