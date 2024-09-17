package com.example.wikisearch.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.AppBarConfiguration.Builder
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wikisearch.R
import com.example.wikisearch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.logging.Handler


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null
    private lateinit var binding: ActivityMainBinding

    companion object{
         lateinit var viewState: MainViewState
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        lateinit var recyclerView: RecyclerView
        lateinit var myAdapter: MyAdapter

        supportActionBar?.setDisplayShowTitleEnabled(false);
        //supportActionBar?.setIcon(R.drawable.app_logo);
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = Builder(navController.graph).build()
        setupActionBarWithNavController(this, navController, appBarConfiguration!!)
        viewState = MainViewState()
        binding.viewState = viewState

        recyclerView = findViewById(R.id.recyclerView)


        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
       val retrofitData = retrofitBuilder.getProductData()
        retrofitData.enqueue(object : Callback<MyData?> {

            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {

                var responseBody = response.body()
                val productList = responseBody?.products!!

                myAdapter = MyAdapter(this@MainActivity, productList)
                recyclerView.adapter = myAdapter
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)


            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("Main Activity", "onFailure:" + t.message)
            }
        })

        val textView = findViewById<TextView>(R.id.textView)

        val retrofitBuilder1 = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData1 = retrofitBuilder1.getProductData()
        retrofitData1.enqueue(object : Callback<MyData?> {

            @SuppressLint("WrongViewCast")
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                val responseBody = response.body()
                val productList = responseBody?.products

                // ... (rest of your code)

                if (productList != null) {
                    val productNames = productList.joinToString(separator = "\n") { it.otd }
                    textView.text = productNames

                    // Add share button
                    val shareButton = findViewById<Button>(R.id.shareButton) // Replace R.id.shareButton with your actual button ID
                    shareButton.setOnClickListener {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_TEXT, productNames)
                        startActivity(Intent.createChooser(shareIntent, "Share products"))
                    }
                    // ... (rest of your code)


                } else {
                    textView.text = "No products found"
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("Main Activity", "onFailure: " + t.message)
                textView.text = "Failed to load data"
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        return (navigateUp(navController, appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }
}