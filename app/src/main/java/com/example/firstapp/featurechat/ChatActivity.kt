package com.example.firstapp.featurechat

import android.Manifest
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firstapp.OnPhotoAdapterListener
import com.example.firstapp.PhotoAdapter
import com.example.firstapp.databinding.ActivityChatBinding
import com.example.firstapp.featurechat.photo.Photo
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.ByteArrayOutputStream


class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var chatList: ArrayList<Chat> = ArrayList()
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var mLinearLayout: LinearLayoutManager

    private lateinit var photoAdapter: PhotoAdapter
    var photoListClicked = ArrayList<Photo>()

    private var colum = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private lateinit var bottomSheetGalleryBehavior: BottomSheetBehavior<View>
    var photoCount = 0

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setEdittextUsableWhenFullScreen()
        setColorActionBar()
        setPermission()
        setRecyclerViewChat()

        binding.ivChatSend.setOnClickListener {
            getText()
        }

        binding.ivChatCamera.setOnClickListener {
            dispatchTakePictureIntent()
        }

        bottomSheetGalleryBehavior = BottomSheetBehavior.from(binding.clChatBottomsheet)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)


        val peekHeight = displayMetrics.heightPixels * 2 / 5
        bottomSheetGalleryBehavior.peekHeight = peekHeight

        binding.ivChatGallery.setOnClickListener {
            binding.tvChatTopsheet.visibility = View.GONE
            bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetGalleryBehavior.isHideable = false
            openGallery()
            binding.coordinatorLayoutChatPicture.visibility = View.VISIBLE
            binding.tvChatSendphotoCount.visibility = View.GONE
            binding.clChatInforsendsheet.visibility = View.VISIBLE

            val param = binding.clChatInforsendsheet.layoutParams as MarginLayoutParams
            param.bottomMargin = peekHeight
            binding.clChatInforsendsheet.layoutParams = param
        }

        binding.ivChatClosesheet.setOnClickListener {
            setHiddenBottomSheet()
        }

        binding.ivChatBacksheet.setOnClickListener {
            setHiddenBottomSheet()
        }
        binding.ivChatSendsheet.setOnClickListener {
            setHiddenBottomSheet()
            getListPhotoClicked()

        }
        binding.btChatSendphoto.setOnClickListener {
            setHiddenBottomSheet()
            getListPhotoClicked()
        }

        bottomSheetGalleryBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("Range")
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {//thu nhỏ đến chiều cao tối thiểu
                        binding.coordinatorLayoutChatPicture.visibility = View.VISIBLE
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {//max
                        binding.coordinatorLayoutChatPicture.visibility = View.VISIBLE

                        if (photoCount > 0) {
                            binding.tvChatSendphotoCount.visibility = View.VISIBLE
                            binding.tvChatSendphotoCount.text = photoCount.toString()
                            binding.btChatSendphoto.isEnabled = true
                        } else {
                            binding.tvChatSendphotoCount.visibility = View.GONE
                            binding.btChatSendphoto.isEnabled = false
                        }
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        photoListClicked.clear()
                        binding.coordinatorLayoutChatPicture.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {}
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {}
                    BottomSheetBehavior.STATE_SETTLING -> {}
                }
            }

            @SuppressLint("Range")
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                var slideOff = slideOffset
                if (slideOff < 0) {
                    slideOff = 0f
                }
                val params = binding.tvChatTopsheet.layoutParams
                params.height = (convertDpToPixel(this@ChatActivity, 88) * slideOff).toInt()
                binding.tvChatTopsheet.layoutParams = params
                if (params.height == 0) {
                    binding.tvChatTopsheet.visibility = View.GONE
                } else {
                    binding.tvChatTopsheet.visibility = View.VISIBLE
                }

                val paramsLine = binding.viewChatContainview.layoutParams
                paramsLine.height =
                    (convertDpToPixel(this@ChatActivity, 20) * (1 - slideOff)).toInt()
                binding.viewChatContainview.layoutParams = paramsLine
                if (paramsLine.height == 0) {
                    binding.viewChatContainview.visibility = View.GONE
                } else {
                    binding.viewChatSheet.visibility = View.VISIBLE
                    binding.viewChatContainview.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun setHiddenBottomSheet() {
        bottomSheetGalleryBehavior.isHideable = true
        binding.clChatInforsendsheet.visibility = View.GONE
        bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.coordinatorLayoutChatPicture.visibility = View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (bottomSheetGalleryBehavior.state == BottomSheetBehavior.STATE_COLLAPSED || bottomSheetGalleryBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetGalleryBehavior.isHideable = true
            binding.clChatInforsendsheet.visibility = View.GONE
            bottomSheetGalleryBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            super.onBackPressed()
        }
    }

    private fun setEdittextUsableWhenFullScreen() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.viewChatParent) { v, insets ->
            val animator =
                ValueAnimator.ofInt(0, insets.getInsets(WindowInsetsCompat.Type.ime()).bottom)
            animator.addUpdateListener { valueAnimator ->
                v.setPadding(0, 0, 0, valueAnimator.animatedValue as? Int ?: 0)
            }
            animator.duration = 200
            animator.start()
            insets
        }
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

    private fun setRecyclerViewChat() {
        chatAdapter = ChatAdapter(this@ChatActivity, chatList)
        mLinearLayout = LinearLayoutManager(this@ChatActivity, LinearLayoutManager.VERTICAL, false)
        mLinearLayout.stackFromEnd = true
        binding.rvChatChattogether.layoutManager = mLinearLayout
        binding.rvChatChattogether.adapter = chatAdapter
    }

    private fun setColorActionBar() {
        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            this@ChatActivity.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setPermission() {
        if (ActivityCompat.checkSelfPermission(
                this@ChatActivity, colum[0]
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@ChatActivity, colum[1]
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

        val layoutManager = GridLayoutManager(this@ChatActivity, 3)
        binding.rvChatPhotoList.layoutManager = layoutManager
        val divider = GridSpacingItemDecoration(convertDpToPixel(this@ChatActivity, 1), 3)
        binding.rvChatPhotoList.addItemDecoration(divider)

        val photoList = getAllImagesFromDevice(this)
        val mPhotoList = ArrayList<Photo>()
        for (element in photoList) {
            val photo = Photo(
                element, false
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
                    binding.btChatSendphoto.isEnabled = true
                    binding.tvChatSendphotoCount.text = photoCount.toString()
                } else {
                    binding.btChatSendphoto.isEnabled = false
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
        startActivityForResult(cameraIntent, 123)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123 && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val myChat = Chat(
                0, "", SEND_PHOTOS, getImageUriFromBitmap(this@ChatActivity, imageBitmap), null, 0
            )
            chatList.add(myChat)
            binding.rvChatChattogether.smoothScrollToPosition(chatAdapter.itemCount)
            chatAdapter.notifyDataSetChanged()
        }
    }
}