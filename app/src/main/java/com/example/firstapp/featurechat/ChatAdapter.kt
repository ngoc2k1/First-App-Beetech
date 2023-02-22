package com.example.firstapp.featurechat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firstapp.databinding.ItemChatReceiveBinding
import com.example.firstapp.databinding.ItemChatSendBinding

class ChatAdapter(
    private var context: Context, var listMessageChat: ArrayList<Chat>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemChatReceiveVH(
        val bindingReceive: ItemChatReceiveBinding
    ) : RecyclerView.ViewHolder(bindingReceive.root)

    class ItemChatSendVH(
        val bindingSend: ItemChatSendBinding
    ) : RecyclerView.ViewHolder(bindingSend.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) return ItemChatReceiveVH(
            ItemChatReceiveBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
        return ItemChatSendVH(
            ItemChatSendBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == 0) {
            var userFeatureViewHolder = holder as ItemChatReceiveVH
            userFeatureViewHolder.bindingReceive.tvItemReceiveMess.text =
                listMessageChat[position].message
        } else {
            val userFeatureViewHolder = holder as ItemChatSendVH
            userFeatureViewHolder.bindingSend.tvItemSendMess.text =
                listMessageChat[position].message
        }
    }

    override fun getItemCount(): Int {
        return listMessageChat.size
    }

    override fun getItemViewType(position: Int): Int {
        if (listMessageChat[position].isSend == 0) {
            return 0
        }
        return 1
    }
}