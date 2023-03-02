package com.example.firstapp.featurechat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapp.R
import com.example.firstapp.databinding.ActivityChatBinding
import java.util.ArrayList

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var chatList: ArrayList<Chat> = ArrayList()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var mLinearLayout: LinearLayoutManager

    var colum = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setColorActionBar()

        chatAdapter = ChatAdapter(this@ChatActivity, chatList)
        mLinearLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLinearLayout.stackFromEnd = true
        binding.rvChatChattogether.layoutManager = mLinearLayout
        binding.rvChatChattogether.adapter = chatAdapter

        binding.ivChatSend.setOnClickListener {
            sendText()
        }

        binding.ivChatGallery.setOnClickListener {
            openGallery()
        }

        if (ActivityCompat.checkSelfPermission(
                this, colum[0]
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, colum[1]
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(colum, 123)
            }
        }

    }

    private fun setColorActionBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this@ChatActivity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this@ChatActivity.resources.getColor(R.color.title_chat)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == 123) {
            val pictureList: ArrayList<Uri>? = ArrayList()
            if (data!!.clipData != null) {
                val pictureCount = data.clipData!!.itemCount
                for (i in 0 until pictureCount) {
                    pictureList!!.add(data.clipData!!.getItemAt(i).uri)
                }
                chatList.add(Chat(0, "", 0, null, pictureList, SEND_MULTIPHOTOS))
                chatAdapter.notifyDataSetChanged()

            } else if (data.data != null) {
                val imageUri = data.data
                val myChat = Chat(0, "", SEND_PHOTOS, imageUri, null, 0)
                chatList.add(myChat)
                chatAdapter.notifyDataSetChanged()
            }
            binding.rvChatChattogether.smoothScrollToPosition(chatAdapter.itemCount)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sendText() {
        val myMessageChat = binding.edtChatInputuser.text.toString()
        if (myMessageChat.isNotEmpty()) {
            val myChat = Chat(SEND_TEXT, myMessageChat, 0, null, null, 0)
            chatList.add(myChat)
            val data = Chat(
                RECEIVE_TEXT, "Xin chào bạn. Rất vui làm quen với bạn.", 0, null, null, 0
            )
            chatList.add(data)
            chatAdapter.notifyDataSetChanged()
            binding.rvChatChattogether.smoothScrollToPosition(chatAdapter.itemCount)
            binding.edtChatInputuser.text?.clear()
        }
    }
}