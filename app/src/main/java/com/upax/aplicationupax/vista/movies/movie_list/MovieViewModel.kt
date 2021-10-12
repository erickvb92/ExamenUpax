package com.upax.aplicationupax.vista.movies.movie_list
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upax.aplicationupax.BuildConfig
import com.upax.aplicationupax.api.Repository
import com.upax.aplicationupax.model.Results
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MovieViewModel : ViewModel() {

    private val TAG = this::class.java.simpleName
    val _loading = MutableLiveData<Boolean>()
    val _error = MutableLiveData<String>()
    val _popular = MutableLiveData<kotlin.collections.List<Results>>()
    val _now = MutableLiveData<kotlin.collections.List<Results>>()

    fun getPopular() {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Repository.getRepositoryApi().getRequestPopular()
                _loading.postValue(false)
                if (response.isSuccessful && response.code() == 200) {
                    val ServiceResponse = response.body() ?: null

                    if (ServiceResponse != null) {
                        Log.d(
                            TAG,
                            "DESCRIPCION DEL SERVICIO : ${ServiceResponse.results[0].title}"
                        )
                    }
                    if (ServiceResponse != null) {

                        val enums: Collection<Results> = ServiceResponse.results
                        Log.d(TAG, "RESPONSE userObject size: ${enums.size}")
                        val lista = ArrayList<Results>()
                        var x = 0;
                        enums.forEach { temp ->
                            lista.add(temp)
                        }

                        Log.d(TAG, "RESPONSE lista.toList: ${lista.toList()}")
                        _popular.postValue(lista.toList())
                        Log.d(TAG, "RESPONSE _messages: ${_popular}")
                    } else {
                        Log.d(
                            TAG,
                            "RESPONSE ERROR "
                        )
                    }

                } else
                    _error.postValue("Error al obtener la información del servidor")
            } catch (e: Exception) {
                _loading.postValue(false)
                _error.postValue("Error al obtener la información del servidor")
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "Error: ${e.printStackTrace()}")
            }
        }
    }

    fun getNow() {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Repository.getRepositoryApi().getRequestNow()
                _loading.postValue(false)
                if (response.isSuccessful && response.code() == 200) {
                    val ServiceResponse = response.body() ?: null

                    if (ServiceResponse != null) {
                        Log.d(
                            TAG,
                            "DESCRIPCION DEL SERVICIO : ${ServiceResponse.results[0].title}"
                        )
                    }
                    if (ServiceResponse != null) {

                        val enums: Collection<Results> = ServiceResponse.results
                        Log.d(TAG, "RESPONSE userObject size: ${enums.size}")
                        val lista = ArrayList<Results>()
                        var x = 0;
                        enums.forEach { temp ->
                            lista.add(temp)
                        }

                        Log.d(TAG, "RESPONSE lista.toList: ${lista.toList()}")
                        _now.postValue(lista.toList())
                        Log.d(TAG, "RESPONSE _messages: ${_now}")
                    } else {
                        Log.d(
                            TAG,
                            "RESPONSE ERROR "
                        )
                    }

                } else
                    _error.postValue("Error al obtener la información del servidor")
            } catch (e: Exception) {
                _loading.postValue(false)
                _error.postValue("Error al obtener la información del servidor")
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "Error: ${e.printStackTrace()}")
            }
        }
    }

}

