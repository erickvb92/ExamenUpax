package com.upax.aplicationupax.vista.movies.movie_list
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.upax.aplicationupax.R
import com.upax.aplicationupax.model.Results
import com.upax.aplicationupax.utils.LoadingMessage
import kotlinx.android.synthetic.main.movies_list.*
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener
import java.util.ArrayList


class MovieActivity : LoadingMessage() {


    private lateinit var viewModel: MovieViewModel
    private lateinit var PopularAdapter: AdapterPopular
    private lateinit var NowAdapter: AdapterNow
    var lista = ArrayList<Results>()
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
            lista = mess as ArrayList<Results>
            val database = FirebaseDatabase.getInstance()
            var myRef = database.getReference("message")

            myRef.setValue(lista)
            // Read from the database
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    NowAdapter.addList(mess)

                    val value = dataSnapshot.getValue()
                    Log.d(TAG, "Value is: $value")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException())
                }
            })
            //NowAdapter.addList(mess)

            //  Toast.makeText(activity, ""+mess.get(1), Toast.LENGTH_LONG).show()
        })
    }

    override fun onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
