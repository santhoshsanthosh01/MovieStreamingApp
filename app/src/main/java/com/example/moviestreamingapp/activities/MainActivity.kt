package com.example.moviestreamingapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.moviestreamingapp.R
import com.example.moviestreamingapp.fragments.FavouritesFragment
import com.example.moviestreamingapp.fragments.MovieFragment
import com.example.moviestreamingapp.fragments.SearchFragment
import com.example.moviestreamingapp.fragments.SeriesFragment
import com.example.moviestreamingapp.repositary.MovieRepo
import com.example.moviestreamingapp.request.MoviesApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var toolbar: Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottom_nav)
        toolbar = findViewById(R.id.toolbar_main)





        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MovieFragment())
            .commit()
        toolbar.setTitle("Movies")
        bottomNavigationView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_movie -> if (supportFragmentManager.findFragmentById(R.id.fragment_container)!!.javaClass.simpleName != "MovieFragment") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, MovieFragment()).commit()
                    toolbar.setTitle("Movies")
                }
                R.id.nav_series -> if (supportFragmentManager.findFragmentById(R.id.fragment_container)!!.javaClass.simpleName != "SeriesFragment") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SeriesFragment()).commit()
                    toolbar.setTitle("Series")
                }
                R.id.nav_search -> if (supportFragmentManager.findFragmentById(R.id.fragment_container)!!.javaClass.simpleName != "SearchFragment") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SearchFragment()).commit()
                    toolbar.setTitle("Search")
                }
                R.id.nav_favourites -> if (supportFragmentManager.findFragmentById(R.id.fragment_container)!!.javaClass.simpleName != "FavouritesFragment") {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FavouritesFragment()).commit()
                    toolbar.setTitle("Favourites")
                }
            }
            true
        })
    }
}