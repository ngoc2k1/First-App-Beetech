package com.example.firstapp.featurechat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapp.OnPhotoAdapterListener
import com.example.firstapp.PhotoAdapter
import com.example.firstapp.R
import com.example.firstapp.databinding.ActivityChatBinding
import com.example.firstapp.featurechat.photo.Photo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.ByteArrayOutputStream


class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var chatList: ArrayList<Chat> = ArrayList()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var mLinearLayout: LinearLayoutManager
    private lateinit var cameraActivityResultLauncher: ActivityResultLauncher<Intent>

    private lateinit var photoAdapter: PhotoAdapter
    var photoListClicked = ArrayList<Photo>()

    private var colum = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    )
    var photoCount = 0

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setColorActionBar()
        setPermission()
        setRecyclerViewChat()

        binding.ivChatSend.setOnClickListener {
            getText()
        }

        binding.ivChatCamera.setOnClickListener {
            dispatchTakePictureIntent()
            handleCameraActivityResult()
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.clChatBottomsheet)

        binding.ivChatGallery.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            openGallery()
            binding.coordinatorLayoutChatPicture.visibility = View.VISIBLE
            binding.tvChatShadowsheet.visibility = View.GONE
        }

        binding.ivChatClosesheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.ivChatBacksheet.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.ivChatSendsheet.setOnClickListener {
            getListPhotoClicked()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.ivChatSendphoto.setOnClickListener {
            getListPhotoClicked()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {//thu nhỏ đến chiều cao tối thiểu
                        binding.coordinatorLayoutChatPicture.visibility = View.VISIBLE
                        binding.groupChatInforsend.visibility = View.GONE
                        binding.groupChatTopsheet.visibility = View.GONE
                        binding.groupChatInforsendsheet.visibility = View.VISIBLE
                        binding.groupChatBottomsheet.visibility = View.GONE

                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {//max
                        binding.groupChatTopsheet.visibility = View.VISIBLE
                        binding.groupChatInforsendsheet.visibility = View.GONE
                        binding.groupChatBottomsheet.visibility = View.VISIBLE
                        binding.coordinatorLayoutChatPicture.visibility = View.VISIBLE

                        if (photoCount > 0) {
                            binding.tvChatSendphotoCount.visibility = View.VISIBLE
                            binding.tvChatSendphotoCount.text = photoCount.toString()
                        } else {
                            binding.tvChatSendphotoCount.visibility = View.GONE
                        }
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        photoListClicked.clear()
                        binding.groupChatTopsheet.visibility = View.GONE
                        binding.groupChatBottomsheet.visibility = View.GONE
                        binding.coordinatorLayoutChatPicture.visibility = View.GONE
                        binding.groupChatInforsend.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getListPhotoClicked() {
        val pictureList: ArrayList<Uri> = ArrayList()
        if (photoListClicked.size > 1) {
            for (i in 0 until photoListClicked.size) {
                pictureList.add(photoListClicked[i].uri)
            }
            chatList.add(Chat(0, "", 0, null, pictureList, SEND_MULTIPHOTOS))

        } else if (photoListClicked.size == 1) {
            val imageUri = photoListClicked[0].uri
            val myChat = Chat(0, "", SEND_PHOTOS, imageUri, null, 0)
            chatList.add(myChat)
        }
        binding.rvChatChattogether.smoothScrollToPosition(chatAdapter.itemCount)
        chatAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleCameraActivityResult() {
        cameraActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                val myChat = Chat(
                    0, "", SEND_PHOTOS, getImageUriFromBitmap(this, imageBitmap), null, 0
                )
                chatList.add(myChat)
                binding.rvChatChattogether.smoothScrollToPosition(chatAdapter.itemCount)
                chatAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setRecyclerViewChat() {
        chatAdapter = ChatAdapter(this@ChatActivity, chatList)
        mLinearLayout = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLinearLayout.stackFromEnd = true
        binding.rvChatChattogether.layoutManager = mLinearLayout
        binding.rvChatChattogether.adapter = chatAdapter
    }

    private fun setColorActionBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this@ChatActivity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this@ChatActivity.resources.getColor(R.color.title_chat)
        }
    }

    private fun setPermission() {
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

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun openGallery() {

        val layoutManager = GridLayoutManager(this, 3)
        binding.rvChatPhotoList.layoutManager = layoutManager
        val divider = GridSpacingItemDecoration(convertDpToPixel(1), 3)
        binding.rvChatPhotoList.addItemDecoration(divider)

        val photoList = getAllImagesFromDevice(this)
        val mPhotoList = ArrayList<Photo>()
        for (element in photoList) {
            val photo = Photo(
                element,
                false
            )
            mPhotoList.add(photo)
        }
        photoAdapter = PhotoAdapter(this@ChatActivity, mPhotoList)
        binding.rvChatPhotoList.adapter = photoAdapter
        photoAdapter.notifyDataSetChanged()

        photoCount = 0
        photoAdapter.onClickListener = object : OnPhotoAdapterListener {
            override fun pickPhoto(photo: Photo) {
                val checkPhotoClicked = photo.isClicked
                if (checkPhotoClicked) {
                    photoCount -= 1
                    photo.isClicked = false
                    photoListClicked.remove(photo)
                }
                if (!checkPhotoClicked) {
                    photoCount += 1
                    photo.isClicked = true
                    photoListClicked.add(photo)
                }
                if (photoCount > 0) {
                    binding.tvChatSendphotoCount.visibility = View.VISIBLE
                    binding.tvChatSendphotoCount.text = photoCount.toString()
                } else {
                    binding.tvChatSendphotoCount.visibility = View.GONE
                }
            }
        }
    }

    private fun getAllImagesFromDevice(context: Context): List<Uri> {
        val imageUris = mutableListOf<Uri>()

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val selection: String? = null
        val selectionArgs: Array<String>? = null

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imageUris.add(imageUri)
            }
        }
        return imageUris
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getText() {
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

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraActivityResultLauncher.launch(cameraIntent)
        handleCameraActivityResult()
    }
}