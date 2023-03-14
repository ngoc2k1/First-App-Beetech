package com.example.firstapp.featureauthen

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.databinding.ActivityLoginBinding
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.log


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
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
                ?.subscribe(object : Observer<LoginRequest?> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                        Log.d("ngocnext", "onSubscribe")
                    }

                    override fun onNext(apiResponse: LoginRequest) {
                        Log.d("ngocnext", "onNext: $apiResponse")
                    }

                    override fun onError(e: Throwable) {
                        Log.d("ngocnext", ":onError $e")
                    }

                    override fun onComplete() {
                        Log.d("ngocnext", ":onComplete ")
                    }
                })
        }
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