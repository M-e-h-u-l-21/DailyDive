package com.example.mvvmlearning.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mvvmlearning.R
import com.example.mvvmlearning.db.ArticleDataBase
import com.example.mvvmlearning.models.Article
import com.example.mvvmlearning.repository.NewsRepository
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        val navController=navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(navController)

        val newsRepository=NewsRepository(ArticleDataBase(this))
        val viewModelProviderFactory=NewsViewModelProviderFactory(newsRepository = newsRepository )
        viewModel= ViewModelProvider(this,viewModelProviderFactory)[NewsViewModel::class.java]


    }
}