package com.example.mvvmlearning.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.mvvmlearning.R
import com.example.mvvmlearning.adapters.NewsAdapter
import com.example.mvvmlearning.databinding.FragmentBreakingnewsBinding
import com.example.mvvmlearning.ui.NewsActivity
import com.example.mvvmlearning.ui.NewsViewModel
import com.example.mvvmlearning.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.mvvmlearning.util.Resource

class BreakingNewsFragment : Fragment() {
    private lateinit var binding: FragmentBreakingnewsBinding
    lateinit var viewModel:NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    val TAG="BreakingNewsFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_breakingnews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentBreakingnewsBinding.bind(view)
        viewModel=(activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
        viewModel.breakingNews.observe( viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let{newsResponse ->
                    newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE +2
                            isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage){
                                binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let {message->
                        Toast.makeText(activity,"An error occured $message",Toast.LENGTH_LONG).show()
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
        isLoading = false
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }

    var isLoading=false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount=layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter=NewsAdapter()
        binding.rvBreakingNews.apply{
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }


    }

}