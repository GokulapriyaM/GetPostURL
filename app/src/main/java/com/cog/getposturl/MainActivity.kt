package com.cog.getposturl

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.support.design.widget.Snackbar
import android.util.Log
import android.widget.TextView
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Created by GokulaPriya-GP on 04/06/2018
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        post.setOnClickListener({
            postURL("gokulapriya@cogzidel.com","0aA0PWbd2QY5TXaHtjK4UOY9r6c2","Priya","Dharshini","https://lh6.googleusercontent.com/-hb3XJBsDChM/AAAAAAAAAAI/AAAAAAAAAAs/EKuehlb4RwY/s96-c/photo.jpg")
        })
        get.setOnClickListener({
            //MonogoId in Database
            getURL("5afbcacbf4e73a00140301d4")
        })

    }


    /**
     *GetURL main function
     */
    fun getURL(mongoId:String) {
        val getUrld = "https://justdeployengine-elinor.herokuapp.com/users/profile/$mongoId"
        val request = Request.Builder().url(getUrld).build()
        val gitClientd = OkHttpClient()
        gitClientd.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                val bitFailure = Snackbar.make(get, "Error In GetURL Response Failure", Snackbar.LENGTH_LONG)
                bitFailure.show()
            }
            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                println(body)
                Log.i("GetURL Response==>", body.toString())
                try {
                val json = JSONObject(body)
                val status = json.getString("status")
                if (status.equals("200")) {
                    val getURLJson = Snackbar.make(post, "GetURL Response $body", Snackbar.LENGTH_LONG)
                    val snackbarView = getURLJson.getView()
                    val textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
                    textView.maxLines = 15
                    getURLJson.show()
                } else {
                    val getURLJsonError = Snackbar.make(get, "GetURL Response Error in Success", Snackbar.LENGTH_LONG)
                    getURLJsonError.show()
                }}catch (jsonError: JSONException){
                    jsonError.printStackTrace()
                    val getURLJsonError = Snackbar.make(get, "GetURL Response Error ", Snackbar.LENGTH_LONG)
                    getURLJsonError.show()
                }
            }
        })


    }



    /**
     * PostURL Mainfunction
     */
    public fun postURL(fEmail: String, fUid: String, fFirstname: String, fLastname: String, fProfileImg: String) {

        val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()

        println("PostURL Argument Variable values==> $fEmail $fFirstname $fLastname $fUid $fProfileImg")
        val httpUrl = HttpUrl.Builder()
                .scheme("https")
                //Eg:ormbot-engine.herokuapp.com  https://justdeploy-engine.herokuapp.com/google-login
                .host("justdeployengine-elinor.herokuapp.com")
                .addPathSegment("users")
                .addPathSegment("google_login")
                .build()

        val form = FormBody.Builder()
                .add("email", fEmail)
                .add("googleId", fUid)
                .add("firstName", fFirstname)
                .add("lastName", fLastname)
                .add("profileImage", fProfileImg)
                .build()


        try {
            updateResult(doSyncPost(okHttpClient, httpUrl, form))

        } catch (e: IOException) {
            e.printStackTrace()
            val postURLJsonError = Snackbar.make(post, "PostURL Response Error", Snackbar.LENGTH_LONG)
            postURLJsonError.show()

        }

    }

    /**
     * PostURL
     */
    public fun updateResult(myResponse: String) {
        Log.i("PostURL Response==>", myResponse)
        try {
            val json = JSONObject(myResponse)
            println("status ==> ${json.getString("status")}")
            val status = json.getString("status")
            if (status.equals("200")) {
                val postURLJson = Snackbar.make(post, "PostURL Response $myResponse", Snackbar.LENGTH_LONG)
                val snackbarView = postURLJson.getView()
                val textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text) as TextView
                textView.maxLines = 15
                postURLJson.show()

            } else {
                val postURLJsonError = Snackbar.make(post, "PostURL Response Error", Snackbar.LENGTH_LONG)
                postURLJsonError.show()

            }}catch (jsonError: JSONException) {
            jsonError.printStackTrace()
            val postURLJsonError = Snackbar.make(post, "PostURL Response Error in Success", Snackbar.LENGTH_LONG)
            postURLJsonError.show()

        }

    }


    /**
     * PostURL
     */
    @Throws(IOException::class)
    fun doSyncPost(client: OkHttpClient, url: HttpUrl, body: RequestBody): String {
        return doSyncPost(client, url.toString(), body)
    }

    /**
     * PostURL
     */
    @Throws(IOException::class)
    fun doSyncPost(client: OkHttpClient, url: String, body: RequestBody): String {
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val response = client.newCall(request).execute()
        return response.body()?.string().toString()
    }

}
