package com.example.lab_week_05

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.ImageView
import com.example.lab_week_05.api.CatApiService
import com.example.lab_week_05.model.ImageData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : AppCompatActivity() {

    private val retrofit by lazy {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val catApiService by lazy {
        retrofit.create(CatApiService::class.java)
    }

    private val apiResponseView: TextView by lazy {
        findViewById(R.id.api_response)
    }

    private val imageResultView: ImageView by lazy {
        findViewById(R.id.image_result)
    }

    private val imageLoader: ImageLoader by lazy {
        GlideLoader(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCatImageResponse()
    }

    private fun getCatImageResponse() {
        apiResponseView.text = "Loading..."

        val call = catApiService.searchImages(10)
        call.enqueue(object : Callback<List<ImageData>> {
            override fun onFailure(call: Call<List<ImageData>>, t: Throwable) {
                Log.e(MAIN_ACTIVITY, "Failed to get response", t)
                apiResponseView.text = "Error: ${t.message}"
            }

            override fun onResponse(call: Call<List<ImageData>>, response: Response<List<ImageData>>) {
                if (response.isSuccessful) {
                    val images = response.body()
                    val validImages = images?.filter { !it.imageUrl.isNullOrBlank() }

                    val imageUrl = if (validImages.isNullOrEmpty()) {
                        "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
                    } else {
                        validImages.first().imageUrl!!
                    }

                    // Load gambar ke ImageView
                    if (imageUrl.isNotBlank()) {
                        imageLoader.loadImage(imageUrl, imageResultView)
                    } else {
                        Log.d(MAIN_ACTIVITY, "Missing image URL")
                    }

                    // Ambil breed name, kalau tidak ada tampilkan "Unknown"
                    val breedName = if (!validImages.isNullOrEmpty() &&
                        !validImages.first().breeds.isNullOrEmpty()) {
                        validImages.first().breeds?.first()?.name ?: "Unknown"
                    } else {
                        "Unknown"
                    }

                    apiResponseView.text = "Breed: $breedName"
                } else {
                    apiResponseView.text = "Error: ${response.code()}"
                }
            }
        })
    }

    companion object {
        const val MAIN_ACTIVITY = "MAIN_ACTIVITY"
    }
}