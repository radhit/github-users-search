package com.example.riskaadhita.base

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.riskaadhita.R
import com.example.riskaadhita.helper.RecyclerViewClickListener
import com.example.riskaadhita.helper.RecyclerViewTouchListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.healthmate.api.BaseApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

abstract class BaseActivity: AppCompatActivity() {
    abstract fun getView(): Int
    lateinit var baseApi: BaseApi
    lateinit var retrofit: Retrofit
    protected lateinit var gson: Gson
    protected var requestOptions = RequestOptions().placeholder(R.drawable.loading_github).error(R.drawable.img_not_found).diskCacheStrategy(
            DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        createRetrofit()
        baseApi = retrofit.create(BaseApi::class.java)
        setContentView(getView())
        observeVM()
        onActivityCreated(savedInstanceState)
    }

    private fun createRetrofit() {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        var httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
                .readTimeout(2000000, TimeUnit.SECONDS)
                .writeTimeout(200000, TimeUnit.SECONDS)
                .connectTimeout(200000, TimeUnit.SECONDS)

        httpClient.addInterceptor(logging)

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    abstract fun onActivityCreated(savedInstanceState: Bundle?)

    init {
        createGson()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }


    open fun observeVM(){ }

    open fun startLoading(){}
    open fun finishLoading(){}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createGson() {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Float::class.java, object : TypeAdapter<Float>() {
            @Throws(IOException::class)
            override fun read(reader: JsonReader): Float? {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull()
                    return null
                }
                try {
                    return java.lang.Float.valueOf(reader.nextString())
                } catch (e: NumberFormatException) {
                    return null
                }

            }

            @Throws(IOException::class)
            override fun write(writer: JsonWriter, value: Float?) {
                if (value == null) {
                    writer.nullValue()
                    return
                }
                writer.value(value)
            }

        })
        gsonBuilder.registerTypeAdapter(Double::class.java, object : TypeAdapter<Double>() {
            @Throws(IOException::class)
            override fun read(reader: JsonReader): Double? {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull()
                    return 0.0
                }
                try {
                    return java.lang.Double.valueOf(reader.nextString())
                } catch (e: NumberFormatException) {
                    return 0.0
                }

            }

            @Throws(IOException::class)
            override fun write(writer: JsonWriter, value: Double?) {
                if (value == null) {
                    writer.nullValue()
                    return
                }
                writer.value(value)
            }
        })
        gson = gsonBuilder.create()
    }

    fun initiateLinearLayoutRecyclerView(recyclerView: RecyclerView, clickListener: RecyclerViewClickListener?){
        recyclerView.layoutManager = LinearLayoutManager(this)
        val mLayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(mLayoutManager)
        recyclerView.addOnItemTouchListener(RecyclerViewTouchListener(this, recyclerView, clickListener))
    }
}