package com.example.firstapp.featurechat

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapp.R
import com.example.firstapp.databinding.ActivityChatBinding
import java.util.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var chatList: ArrayList<Chat> = ArrayList()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var mLinearLayout: LinearLayoutManager

    private var imageUri: Uri? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this@ChatActivity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this@ChatActivity.resources.getColor(R.color.title_chat)
        }

        chatAdapter = ChatAdapter(this@ChatActivity, chatList)
        mLinearLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLinearLayout.stackFromEnd = true
        binding.rvChatChattogether.layoutManager = mLinearLayout
        binding.rvChatChattogether.adapter = chatAdapter

        binding.ivChatSend.setOnClickListener {
            val myMessageChat = binding.edtChatInputuser.text.toString()
            if (myMessageChat.isNotEmpty()) {
                val myChat = Chat(ChatAction.SEND.original, myMessageChat, 0, null)
                chatList.add(myChat)
                val data = Chat(ChatAction.RECEIVE.original, "Xin chào bạn. Rất vui làm quen với bạn.", 0, null)
                chatList.add(data)
                chatAdapter.notifyDataSetChanged()
                binding.rvChatChattogether.smoothScrollToPosition(chatAdapter.itemCount)
                binding.edtChatInputuser.text?.clear()
            }
        }

        binding.ivChatGallery.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 1)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            imageUri = data?.data

            val myChat = Chat(3, "", ChatAction.SEND_PHOTO.original, imageUri)
            chatList.add(myChat)
            chatAdapter.notifyDataSetChanged()
            binding.rvChatChattogether.smoothScrollToPosition(chatAdapter.itemCount)

        }
    }
}