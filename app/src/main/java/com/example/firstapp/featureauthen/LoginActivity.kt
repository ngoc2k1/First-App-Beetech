package com.example.firstapp.featureauthen

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.InputType.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.databinding.ActivityLoginBinding
import com.example.firstapp.featurechat.ChatActivity
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: ProgressDialog
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setColorActionBar()

        binding.btLoginLogin.setOnClickListener {
            val username = binding.edtLoginUsername.text.toString()
            val password = binding.edtLoginPassword.text.toString()
            val loginRequest = LoginRequest(username, password, "asdadadsadaa", 2)
            ApiClient.chatService.createAccount("2.0.0", 2, loginRequest)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<LoginResponse?> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                        loadingDialog = ProgressDialog.show(
                            this@LoginActivity, "",
                            "Loading. Please wait...", true
                        )
                    }

                    override fun onNext(loginResponse: LoginResponse) {
                        loadingDialog.dismiss()
                        if (loginResponse.msg == "Đăng nhập thành công!") {
                            startActivity(Intent(this@LoginActivity, ChatActivity::class.java))
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        loadingDialog.dismiss()
                        Toast.makeText(
                            this@LoginActivity,
                            throwable.getErrorBody().errorMessage.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(
                            "error",
                            "onError: " + throwable.getErrorBody().errorMessage.toString()
                        )
                    }

                    override fun onComplete() {
                    }
                })
        }
    }

    fun Throwable.getErrorBody(): RequestError {
        if (this is HttpException) {
            val body = response()?.errorBody()
            val gson = Gson()
            val adapter = gson.getAdapter(RequestError::class.java)
            try {
                return adapter.fromJson(body?.string())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return RequestError()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

    private fun setColorActionBar() {
        window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            this@LoginActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

            statusBarColor = Color.TRANSPARENT
        }
    }

}