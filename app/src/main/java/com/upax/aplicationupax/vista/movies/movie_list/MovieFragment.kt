package com.upax.aplicationupax.vista.movies.movie_list
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.upax.aplicationupax.R
import com.upax.aplicationupax.model.Results
import com.upax.aplicationupax.vista.movies.base.BaseFragment
import com.upax.aplicationupax.vista.movies.movie_list.AdapterNow
import com.upax.aplicationupax.vista.movies.movie_list.AdapterPopular
import kotlinx.android.synthetic.main.movies_list.*
import java.util.*

class MovieFragment : BaseFragment() {

    companion object {
        fun newInstance() = MovieFragment()
    }
    private lateinit var viewModel: MovieViewModel
    private lateinit var viewFragment: View
    private lateinit var PopularAdapter: AdapterPopular
    private lateinit var NowAdapter: AdapterNow
    var lista = ArrayList<Results>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewFragment = inflater.inflate(R.layout.movies_list, container, false)


        return viewFragment
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]

        PopularAdapter = AdapterPopular(
            requireActivity(),
            arrayListOf(),
            object : AdapterPopular.NotificationEvent {
                override fun onNotificationTouch(notification: Results) {

                }
            })

        val mLayoutManager = LinearLayoutManager(requireActivity())
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewPopular.layoutManager = mLayoutManager
        recyclerViewPopular.itemAnimator = DefaultItemAnimator()


        recyclerViewPopular.adapter = PopularAdapter

        NowAdapter = AdapterNow(
            requireActivity(),
            arrayListOf(),
            object : AdapterNow.NotificationEvent {
                override fun onNotificationTouch(notification: Results) {

                }
            })

        val mLayoutManager2 = LinearLayoutManager(requireActivity())
        mLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewNow.layoutManager = mLayoutManager2
        recyclerViewNow.itemAnimator = DefaultItemAnimator()

        recyclerViewNow.adapter = NowAdapter

        viewModel.getPopular()
        viewModel.getNow()
        setObservers()
    }

    private fun setObservers(){
        viewModel._loading.observe(viewLifecycleOwner, Observer {
            val isLoading = it ?: return@Observer
            //super.showLoading(isLoading)
        })
        viewModel._popular.observe(viewLifecycleOwner, Observer {
            val mess = it ?: return@Observer
            PopularAdapter.addList(mess)
            //  Toast.makeText(activity, ""+mess.get(1), Toast.LENGTH_LONG).show()
        })

        viewModel._now.observe(viewLifecycleOwner, Observer {
            val mess = it ?: return@Observer
            lista = mess as ArrayList<Results>
            val database = FirebaseDatabase.getInstance()
            var myRef = database.getReference("listas")

            myRef.setValue(lista)
            // Read from the database
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    //lista = dataSnapshot.getValue() as ArrayList<Results>

                    Log.d(ContentValues.TAG, "Value is: $lista")
                    NowAdapter.addList(mess)
                }
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })
        })
    }

}
