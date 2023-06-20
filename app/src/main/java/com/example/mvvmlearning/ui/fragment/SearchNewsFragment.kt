package com.example.mvvmlearning.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmlearning.R
import com.example.mvvmlearning.adapters.NewsAdapter
import com.example.mvvmlearning.databinding.FragmentSearchnewsBinding
import com.example.mvvmlearning.ui.NewsActivity
import com.example.mvvmlearning.ui.NewsViewModel
import com.example.mvvmlearning.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.mvvmlearning.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment() {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter:NewsAdapter
    lateinit var binding: FragmentSearchnewsBinding
    val TAG="SearchNewsFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searchnews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSearchnewsBinding.bind(view)
        viewModel=(activity as NewsActivity).viewModel
        setupRecyclerView()

        var job: Job?=null
        binding.etSearch.addTextChangedListener {editable->
            job?.cancel()
            job= MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let{
                    if(editable.toString().isNotEmpty() ){
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }


        viewModel.searchNews.observe( viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let{newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)

                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let {message->
                        Log.e(TAG,"An error occured: $message")
                    }
                }
                is Resource.Loading->{
                    showProgressBar()
                }
            }
        })
    }
    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility=View.INVISIBLE
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdapter=NewsAdapter()
        binding.rvSearchNews.apply{
            adapter=newsAdapter
            layoutManager  = LinearLayoutManager(activity)
        }

    }
}