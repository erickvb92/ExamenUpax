package com.upax.aplicationupax.vista.movies.movie_list
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.android.synthetic.main.movies_list.*
import java.util.*
import android.net.ConnectivityManager
import android.preference.PreferenceManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

class MovieFragment : BaseFragment() {

    companion object {
        fun newInstance() = MovieFragment()
    }
    private lateinit var viewModel: MovieViewModel
    private lateinit var viewFragment: View
    private lateinit var PopularAdapter: AdapterPopular
    private lateinit var NowAdapter: AdapterNow

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

        val wifi = isConnectedWifi()
        val red = isConnectedMobile()
        if(!wifi && !red){
            var list_now =  getArrayList("lista_now")
            if (list_now != null) {
                NowAdapter.addList(list_now)
            }

            var list_popular =  getArrayList("lista_popular")
            if (list_popular != null) {
                PopularAdapter.addList(list_popular)
            }
                    Toast.makeText(
                        activity,
                        "No hay conexion, los ultimos valores se persisten",
                        Toast.LENGTH_SHORT
                    ).show()

        }

    }

    private fun setObservers(){
        viewModel._loading.observe(viewLifecycleOwner, Observer {
            val isLoading = it ?: return@Observer
            //super.showLoading(isLoading)
        })
        viewModel._popular.observe(viewLifecycleOwner, Observer {
            val mess = it ?: return@Observer
            if(mess!=null){
                PopularAdapter.addList(mess)
                saveArrayList(mess as ArrayList<Results>, "lista_popular")
            }else{
                var list =  getArrayList("lista_popular")
                if (list != null) {
                    PopularAdapter.addList(list)
                }
            }
            //  Toast.makeText(activity, ""+mess.get(1), Toast.LENGTH_LONG).show()
        })

        viewModel._now.observe(viewLifecycleOwner, Observer {
            val mess = it ?: return@Observer
            if(mess!=null){
                        NowAdapter.addList(mess)
                saveArrayList(mess as ArrayList<Results>, "lista_now")
            }else{
               var list =  getArrayList("lista_now")
                if (list != null) {
                    NowAdapter.addList(list)
                }
            }
        })
    }

    fun isConnectedWifi(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    fun isConnectedMobile(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE
    }

    fun saveArrayList(list: ArrayList<Results>, key: String?) {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor: SharedPreferences.Editor = prefs.edit()
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    fun getArrayList(key: String?): ArrayList<Results>? {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val gson = Gson()
        val json: String? = prefs.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<Results>?>() {}.getType()
        return gson.fromJson(json, type)
    }
}
