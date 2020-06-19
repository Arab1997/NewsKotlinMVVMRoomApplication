package com.example.newskotlinmvvmapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newskotlinmvvmapplication.R
import com.example.newskotlinmvvmapplication.adapter.NewsDataAdapter
import com.example.newskotlinmvvmapplication.listener.PaginationListener
import com.example.newskotlinmvvmapplication.util.Constants
import com.example.newskotlinmvvmapplication.viewmodel.NewsListActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class NewsListActivity : AppCompatActivity() {

    private val activity = this@NewsListActivity
    private var newsDataAdapter: NewsDataAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private lateinit var viewModel: NewsListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(NewsListActivityViewModel::class.java)

        viewModel.showProgress.observe(this, Observer {
            if (it)
                search_progress.visibility = View.VISIBLE
            else
                search_progress.visibility = View.GONE
        })

        if (Constants.isNetworkAvailable(activity)) {
            viewModel.deleteNewsData()
            viewModel.getNewsData()
        } else {
            Constants.displayToast(activity, resources.getString(R.string.no_internet))
        }

        layoutManager = LinearLayoutManager(this)
        rvNewsList.setHasFixedSize(true)
        rvNewsList.setLayoutManager(layoutManager)

        viewModel.getNewsDataFromDatabase().observe(this, Observer { news ->
            if (newsDataAdapter != null) {
                viewModel.isLoading = false
                newsDataAdapter!!.updateData(news)
            } else {
                newsDataAdapter =
                    NewsDataAdapter(this, news, object : NewsDataAdapter.onCLickListener {
                        override fun onCLick(view: View?, position: Int) {
                        }
                    })
                rvNewsList.adapter = newsDataAdapter
            }

            rvNewsList.addOnScrollListener(object :
                PaginationListener((layoutManager as LinearLayoutManager?)!!) {

                override fun loadMoreItems() {
                    viewModel.isLoading = true
                    viewModel.getNewsData()
                }

                override val isLastPage: Boolean
                    get() = viewModel.isLastPage
                override val isLoading: Boolean
                    get() = viewModel.isLoading

            })
        })
    }
}
