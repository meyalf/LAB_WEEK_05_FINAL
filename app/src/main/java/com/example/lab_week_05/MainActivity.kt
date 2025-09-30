package com.example.lab_week_05

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var apiResponseText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiResponseText = findViewById(R.id.api_response)

        // Retrofit setup
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/")
            .addConverterFactory(ScalarsConverterFactory.create()) // JSON mentah
            .build()

        val service = retrofit.create(CatApiService::class.java)
        val call = service.getRandomCat()

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    apiResponseText.text = response.body()
                } else {
                    apiResponseText.text = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}")
                apiResponseText.text = "Failure: ${t.message}"
            }
        })
    }
}
