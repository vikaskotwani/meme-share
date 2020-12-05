package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.android.volley.toolbox.JsonObjectRequest as JsonObjectRequest1

class MainActivity : AppCompatActivity() {

    var currentImageUrl:String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }
    private fun loadMeme(){
        //Installing the RequestQueue
        val progressBar=findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility=View.VISIBLE
        val queue= Volley.newRequestQueue(this)
        val url="https://meme-api.herokuapp.com/gimme"

        //Request a string response from the provided url
           val jsonObjectRequest= JsonObjectRequest1(
               Request.Method.GET,url,null,
               Response.Listener{response ->
                   currentImageUrl=response.getString("url")

                   val memeImageView =findViewById<ImageView>(R.id.memeImageView)
                   //Glide takes time to load image
                   Glide.with(this).load(currentImageUrl).listener(object:RequestListener<Drawable>{
                       override fun onLoadFailed(
                           e: GlideException?,
                           model: Any?,
                           target: Target<Drawable>?,
                           isFirstResource: Boolean
                       ): Boolean {
                           progressBar.visibility=View.GONE
                           return false
                       }

                       override fun onResourceReady(
                           resource: Drawable?,
                           model: Any?,
                           target: Target<Drawable>?,
                           dataSource: DataSource?,
                           isFirstResource: Boolean
                       ): Boolean{
                           progressBar.visibility=View.GONE
                           return false
                       }
                   }).into(memeImageView)
               },
               Response.ErrorListener{
                   Toast.makeText(this,"Something wen wrong",Toast.LENGTH_LONG).show()
               }
           )
        //Add the Request in the RequestQueue
            queue.add(jsonObjectRequest)
            }


    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Check this meme out $currentImageUrl")
        val chooser=Intent.createChooser(intent,"Share meme with others using..")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}