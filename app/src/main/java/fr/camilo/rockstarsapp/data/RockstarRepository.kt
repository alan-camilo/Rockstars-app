package fr.camilo.rockstarsapp.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import fr.camilo.rockstarsapp.model.Rockstar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RockstarRepository() {
    fun loadRockstars(list: MutableLiveData<ArrayList<Rockstar>>) {
        RockstarApiClient().service.listRockstars().enqueue(object : Callback<ArrayList<Rockstar>> {
            override fun onFailure(call: Call<ArrayList<Rockstar>>, t: Throwable) {
                Log.d("DEBUGLIST", "enqueue onFailure")
            }

            override fun onResponse(call: Call<ArrayList<Rockstar>>, response: Response<ArrayList<Rockstar>>) {
                Log.d("DEBUGLIST", "enqueue onResponse")
                list.value = response.body()
                GlobalScope.launch(Dispatchers.IO) {
                    //dao.insertAll() TODO adapter rockstar entity rockstar
                }
            }

        })
    }
}