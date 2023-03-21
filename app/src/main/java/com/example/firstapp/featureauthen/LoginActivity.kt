package com.example.firstapp.featureauthen

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstapp.databinding.ActivityLoginBinding
import com.example.firstapp.featureauthen.entity.LoginRequest
import com.example.firstapp.featureauthen.entity.LoginResponse
import com.example.firstapp.featureauthen.entity.RequestError
import com.example.firstapp.featureauthen.interceptor.NetworkInterceptor
import com.example.firstapp.featureauthen.interceptor.NoConnectivityException
import com.example.firstapp.featurechat.ChatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loadingDialog: ProgressDialog

    private val callbackManager = CallbackManager.Factory.create()

    private var disposable: Disposable? = null
    var gso: GoogleSignInOptions? = null
    var gsc: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setColorActionBar()
        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this, gso!!)

        var apiClient = ApiClient(this@LoginActivity)

        try {
            val info = packageManager.getPackageInfo(
                "your.package", PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (_: PackageManager.NameNotFoundException) {
        } catch (_: NoSuchAlgorithmException) {
        }

        val loginManager = LoginManager.getInstance()

        loginManager.registerCallback(callbackManager,//loginMana đăng kí vs callback để có kq tra ve
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_LONG).show()
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException) {}
            })

        binding.tvLoginFacebook.setOnClickListener(View.OnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this@LoginActivity, listOf("public_profile"))
        })
        binding.btLoginLogin.setOnClickListener {
            val username = binding.edtLoginUsername.text.toString()
            val password = binding.edtLoginPassword.text.toString()
            val loginRequest = LoginRequest(username, password, "asdadadsadaa", 2)
            apiClient.chatService.createAccount(loginRequest)?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(object : Observer<LoginResponse?> {
                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                        loadingDialog = ProgressDialog.show(
                            this@LoginActivity, "", "Loading. Please wait...", true
                        )
                    }

                    override fun onNext(loginResponse: LoginResponse) {
                        loadingDialog.dismiss()

                        if (loginResponse.code == 200) {
                            Hawk.put(HawkKey.ACCESS_TOKEN, loginResponse.data.accessToken)
                            startActivity(Intent(this@LoginActivity, ChatActivity::class.java))
                        } else if (loginResponse.code in 400..499) {

                        }
                    }

                    override fun onError(throwable: Throwable) {
                        loadingDialog.dismiss()
                        if (throwable is NoConnectivityException) {
                            Toast.makeText(
                                this@LoginActivity, throwable.message, Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                throwable.getErrorBody().errorMessage.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onComplete() {
                    }
                })
        }

        binding.tvLoginGoogle.setOnClickListener {
            val signInIntent = gsc!!.signInIntent
            startActivityForResult(signInIntent, 1000)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.let { it.onActivityResult(requestCode, resultCode, data) }//
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                Toast.makeText(applicationContext, "Login GG success!", Toast.LENGTH_SHORT).show()
            } catch (e: ApiException) {
                Toast.makeText(applicationContext, "Something went wrong!", Toast.LENGTH_SHORT)
                    .show()
            }
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