package com.example.firstapp.featurechat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapp.R
import com.example.firstapp.databinding.ActivityChatBinding
import com.example.firstapp.databinding.ActivityMainBinding
import java.util.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var chatList: ArrayList<Chat> = ArrayList()
    private lateinit var chatAdapter: ChatAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        chatAdapter = ChatAdapter(this@ChatActivity, chatList)
        binding.rvChatChattogether.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvChatChattogether.adapter = chatAdapter

        binding.ivChatSend.setOnClickListener {
            val myMessageChat = binding.edtChatInputuser.text.toString()
            if (myMessageChat.isNotEmpty()) {
                val myChat = Chat(1, myMessageChat)
                chatList.add(myChat)
                val data = Chat(0, "Xin chào bạn. Rất vui làm quen với bạn")
                chatList.add(data)
                chatAdapter.notifyDataSetChanged()
            }
            binding.edtChatInputuser.text?.clear()
        }
    }
}