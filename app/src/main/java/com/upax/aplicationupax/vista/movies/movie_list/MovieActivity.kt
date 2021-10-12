package com.upax.aplicationupax.vista.movies.movie_list
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.upax.aplicationupax.R
import com.upax.aplicationupax.model.Results
import com.upax.aplicationupax.utils.LoadingMessage
import kotlinx.android.synthetic.main.movies_list.*

class MovieActivity : LoadingMessage() {


    private lateinit var viewModel: MovieViewModel
    private lateinit var PopularAdapter: AdapterPopular
    private lateinit var NowAdapter: AdapterNow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movies_list)

        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]

        PopularAdapter = AdapterPopular(
            applicationContext,
            arrayListOf(),
            object : AdapterPopular.NotificationEvent {
                override fun onNotificationTouch(notification: Results) {

                }
            })
        viewModel.getPopular()

        val mLayoutManager = LinearLayoutManager(applicationContext)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewPopular.layoutManager = mLayoutManager
        recyclerViewPopular.itemAnimator = DefaultItemAnimator()


        recyclerViewPopular.adapter = PopularAdapter

        NowAdapter = AdapterNow(
            applicationContext,
            arrayListOf(),
            object : AdapterNow.NotificationEvent {
                override fun onNotificationTouch(notification: Results) {

                }
            })
        viewModel.getNow()

        val mLayoutManager2 = LinearLayoutManager(applicationContext)
        mLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewNow.layoutManager = mLayoutManager2
        recyclerViewNow.itemAnimator = DefaultItemAnimator()

        recyclerViewNow.adapter = NowAdapter

        setObservers()
    }

    private fun setObservers(){

        viewModel._loading.observe(this, Observer {
            val isLoading = it ?: return@Observer
            super.showLoading(isLoading)
        })
        viewModel._popular.observe(this, Observer {
            val mess = it ?: return@Observer
            PopularAdapter.addList(mess)
            //  Toast.makeText(activity, ""+mess.get(1), Toast.LENGTH_LONG).show()
        })

        viewModel._now.observe(this, Observer {
            val mess = it ?: return@Observer
            NowAdapter.addList(mess)
            //  Toast.makeText(activity, ""+mess.get(1), Toast.LENGTH_LONG).show()
        })
    }

    override fun onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
