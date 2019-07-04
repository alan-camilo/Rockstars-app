package fr.camilo.rockstarsapp.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import fr.camilo.rockstarsapp.db.dao.RockstarDao
import fr.camilo.rockstarsapp.db.entity.RockstarEntity
import fr.camilo.rockstarsapp.model.Rockstar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RockstarRepository(val dao: RockstarDao) {

    suspend fun loadRockstars(list: MutableLiveData<ArrayList<Rockstar>>, refresh: Boolean = false) {
        val rockstars = if (!refresh) dao.getAll().map { it.toRockstar() } as ArrayList<Rockstar> else arrayListOf()
        Log.d("DEBUGLIST", "dao getAll $rockstars")
        if (rockstars.size > 0) {
            withContext(Dispatchers.Main) { list.value = rockstars }
        } else {
            RockstarApiClient().service.listRockstars().enqueue(object : Callback<ArrayList<Rockstar>> {
                override fun onFailure(call: Call<ArrayList<Rockstar>>, t: Throwable) {
                    Log.d("DEBUGLIST", "enqueue onFailure")
                }

                override fun onResponse(call: Call<ArrayList<Rockstar>>, response: Response<ArrayList<Rockstar>>) {
                    Log.d("DEBUGLIST", "enqueue onResponse ${response.body()}")
                    //list.value = ArrayList(response.body()?.sortedBy { it.index })
                    val listFromApi: List<RockstarEntity>? =
                        response.body()?.sortedBy { it.index }?.map { it.toRockstarEntity() }
                    GlobalScope.launch(Dispatchers.IO) {
                        val listFromDb = dao.getAll()
                        listFromApi?.forEach { rockstarFromApi ->
                            val l = listFromDb.filter { it._id == rockstarFromApi._id }
                            if (l.isEmpty()) {
                                dao.insert(rockstarFromApi)
                            }
                        }
                        withContext(Dispatchers.Main) { list.value = ArrayList(dao.getAll().map { it.toRockstar() }) }
                    }
                }
            })
        }
    }

    suspend fun loadBookmarks(list: MutableLiveData<ArrayList<Rockstar>>) {
        val bookmarks = dao.getAllBookmarks().map { it.toRockstar() } as ArrayList<Rockstar>
        Log.d("DEBUGLIST", "dao getBookmarks $bookmarks")
        withContext(Dispatchers.Main) { list.value = bookmarks }
    }

    suspend fun addToBookmark(list: MutableLiveData<ArrayList<Rockstar>>, index: Int) {
        val rockstars = list.value!!
        list.value?.get(index)?.bookmark = true
        dao.update(rockstars[index].toRockstarEntity())
    }

    suspend fun removeFromBookmark(list: MutableLiveData<ArrayList<Rockstar>>, index: Int) {
        val rockstars = list.value!!
        list.value?.get(index)?.bookmark = false
        dao.update(rockstars[index].toRockstarEntity())
    }


}

