package com.example.wikisearch.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.AppBarConfiguration.Builder
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wikisearch.R
import com.example.wikisearch.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Timer
import java.util.TimerTask


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


        /*recyclerView = findViewById(R.id.recyclerView)
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
        })*/




        val cardViewAdd = findViewById<CardView>(R.id.addo)

        val retrofitBuilderAdd = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val timerAdd = Timer()
        val refreshIntervalAdd = 10000L // 10 seconds

        val refreshTaskAdd = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    fetchData()
                }
            }

            fun fetchData() {
                val retrofitDataAdd = retrofitBuilderAdd.getProductData()
                retrofitDataAdd.enqueue(object : Callback<MyData?> {

                    @SuppressLint("WrongViewCast")
                    override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                        val responseBody = response.body()
                        val productList = responseBody?.products

                        if (productList != null) {
                            val productContainer = LinearLayout(this@MainActivity)
                            productContainer.orientation = LinearLayout.VERTICAL

                            for (product in productList) {
                                val productText = TextView(this@MainActivity).apply {
                                   // text = product.adddescription ?: "No description available" // Set default text if empty
                                   // textSize = 20f
                                  //  setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
                                }
                                productContainer.addView(productText)

                                val thumbnailImageView = findViewById<ImageView>(R.id.addphoto)
                                // Load the thumbnail image using your image library (e.g., Glide)
                                Glide.with(this@MainActivity).load(product.addphoto).into(thumbnailImageView)

                                val addTitleTextView = findViewById<TextView>(R.id.addtitle)
                                addTitleTextView.text = product.addtitle

                                val addDTextView = findViewById<TextView>(R.id.adddescription)
                                addDTextView.text = product.adddescription


                            }

                            cardViewAdd.addView(productContainer) // Add the container with all products to CardView

                            // Schedule removal after 10 seconds
                            val handler = Handler(Looper.getMainLooper())
                            val runnable = Runnable {
                                cardViewAdd.removeView(productContainer) // Remove the container after 10 seconds
                            }
                            handler.postDelayed(runnable, 10000)

                            // Update share button behavior (assuming productTextNum holds phone number)
                            findViewById<Button>(com.example.wikisearch.R.id.shareButtonadd).setOnClickListener {
                                val phoneNumber =
                                    productList[0].addenquiry // Get the phone number from the first product
                                val message =
                                    findViewById<TextView>(com.example.wikisearch.R.id.messageadd).text.toString()

                                val whatsappUrl =
                                    "https://api.whatsapp.com/send?phone=$phoneNumber&text=${
                                        Uri.encode(message)
                                    }"
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(whatsappUrl)
                                }
                                startActivity(intent)
                            }
                        } else {
                            // Handle no products found case
                        }
                    }

                    override fun onFailure(call: Call<MyData?>, t: Throwable) {
                        Log.d("Main Activity", "onFailure: " + t.message)
                        // Handle network error (e.g., display an error message)
                    }
                })
            }
        }

        timerAdd.schedule(refreshTaskAdd, 0, refreshIntervalAdd)





// BIRTHDAY WISHES

        val cardView1 = findViewById<CardView>(R.id.wish)

        val retrofitBuilder1 = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)


        val timer = Timer()
        val refreshInterval = 10000L // 10 seconds

        val refreshTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    fetchData()
                }
            }


            fun fetchData() {


                val retrofitData1 = retrofitBuilder1.getProductData()
                retrofitData1.enqueue(object : Callback<MyData?> {

                    @SuppressLint("WrongViewCast")
                    override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                        val responseBody = response.body()
                        val productList = responseBody?.products

                        if (productList != null) {
                            val productContainer =
                                LinearLayout(this@MainActivity) // Create a container for product views
                            productContainer.orientation =
                                LinearLayout.VERTICAL // Set vertical orientation for stacking views

                            for (product in productList) {
                                val productText = TextView(this@MainActivity)
                                productText.text = product.wish
                                productText.textSize = 20f // Set desired text size
                                productText.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black)) // Set the desired text color

                                productContainer.addView(productText)

                                // Add phone number view if needed
                                val productTextNum = TextView(this@MainActivity)
                                //productTextNum.text = product.num
                                productContainer.addView(productTextNum)
                            }

                            cardView1.addView(productContainer) // Add the container with all products to CardView

                            // Schedule removal after 10 seconds
                            val handler = Handler(Looper.getMainLooper())
                            val runnable = Runnable {
                                cardView1.removeView(productContainer) // Remove the container after 10 seconds
                            }
                            handler.postDelayed(
                                runnable,
                                10000
                            ) // Delay removal for 10 seconds (1000 milliseconds)

                            // Update share button behavior (assuming productTextNum holds phone number)
                            findViewById<Button>(com.example.wikisearch.R.id.num).setOnClickListener {
                                val phoneNumber =
                                    productList[0].num // Get the phone number from the first product
                                val message =
                                    findViewById<TextView>(com.example.wikisearch.R.id.messageText).text.toString()

                                val whatsappUrl =
                                    "https://api.whatsapp.com/send?phone=$phoneNumber&text=${
                                        Uri.encode(message)
                                    }"
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(whatsappUrl)
                                }
                                startActivity(intent)
                            }
                        } else {
                            // Handle no products found case
                        }
                    }

                    override fun onFailure(call: Call<MyData?>, t: Throwable) {
                        Log.d("Main Activity", "onFailure: " + t.message)
                        // Handle network error (e.g., display an error message)
                    }
                })
            }}
                timer.schedule(refreshTask, 0, refreshInterval)


           // END BIRTHDAY WISHES



        val cardView2 = findViewById<CardView>(com.example.wikisearch.R.id.otd)

        val retrofitBuilder2 = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData2 = retrofitBuilder2.getProductData()
        retrofitData2.enqueue(object : Callback<MyData?> {


            @SuppressLint("WrongViewCast")
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                val responseBody = response.body()
                val productList = responseBody?.products

                // ... (rest of your code)

                if (productList != null) {
                    val productNames = productList.joinToString(separator = "\n") { it.otd }

                    val productText = TextView(this@MainActivity)
                    productText.text = productNames
                    productText.textSize = 20f // Set desired text size

                    // Add TextView to CardView (adjust layout as needed)
                    cardView2.addView(productText)

                    val shareButton = findViewById<Button>(com.example.wikisearch.R.id.shareButton2) // Replace R.id.shareButton with your actual button ID
                    shareButton.setOnClickListener {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_TEXT, productNames)
                        startActivity(Intent.createChooser(shareIntent, "Share products"))
                    }


                } else {
                    // cardView.text = "No products found"
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("Main Activity", "onFailure: " + t.message)
                // cardView.text = "Failed to load data"
            }
        })


        val cardView3 = findViewById<CardView>(com.example.wikisearch.R.id.report)

        val retrofitBuilder3 = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData3 = retrofitBuilder3.getProductData()
        retrofitData3.enqueue(object : Callback<MyData?> {

            @SuppressLint("WrongViewCast")
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                val responseBody = response.body()
                val productList = responseBody?.products

                if (productList != null && productList.isNotEmpty()) {
                    val product = productList[0] // Assuming you want the first product
                    val titleTextView = findViewById<TextView>(com.example.wikisearch.R.id.title)
                    val descriptionTextView = findViewById<TextView>(com.example.wikisearch.R.id.description)
                    val thumbnailImageView = findViewById<ImageView>(com.example.wikisearch.R.id.photo)

                    titleTextView.text = product.title ?: "No title found"
                    descriptionTextView.text = product.description ?: "No description available"

                    // Load the thumbnail image using Picasso (replace with your Glide instance if needed)
                    Picasso.get().load(product.thumbnail).into(thumbnailImageView)
                } else {
                    // Handle case where no products are found
                    // (e.g., display a message or hide the CardView)
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("Main Activity", "onFailure: " + t.message)
                // Handle network error (e.g., display an error message)
            }
        })

        val cardView4 = findViewById<CardView>(com.example.wikisearch.R.id.news)

        val retrofitBuilder4 = Retrofit.Builder()
            .baseUrl("https://advocatepedia.com/feed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        val retrofitData4 = retrofitBuilder4.getProductData()
        retrofitData4.enqueue(object : Callback<MyData?> {


            @SuppressLint("WrongViewCast")
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                val responseBody = response.body()
                val productList = responseBody?.products

                // ... (rest of your code)

                if (productList != null) {
                    val productNames = productList.joinToString(separator = "\n") { it.news }

                    val productText = TextView(this@MainActivity)
                    productText.text = productNames
                    productText.textSize = 20f // Set desired text size

                    // Add TextView to CardView (adjust layout as needed)
                    cardView4.addView(productText)

                    val shareButton = findViewById<Button>(com.example.wikisearch.R.id.shareButton4) // Replace R.id.shareButton with your actual button ID
                    shareButton.setOnClickListener {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_TEXT, productNames)
                        startActivity(Intent.createChooser(shareIntent, "Share products"))
                    }

                } else {
                    // cardView.text = "No products found"
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.d("Main Activity", "onFailure: " + t.message)
                // cardView.text = "Failed to load data"
            }
        })

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(com.example.wikisearch.R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == com.example.wikisearch.R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, com.example.wikisearch.R.id.nav_host_fragment_content_main)
        return (navigateUp(navController, appBarConfiguration!!)
                || super.onSupportNavigateUp())
    }
}