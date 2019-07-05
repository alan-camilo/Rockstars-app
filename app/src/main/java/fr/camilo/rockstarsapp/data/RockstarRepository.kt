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

    /**
     * Retrieves the rockstars list from the database. If the database is empty or $refresh is set at true,
     * makes a request to get the list and saves it in the database
     * @param list
     * @param refresh
     */
    suspend fun loadRockstars(list: MutableLiveData<ArrayList<Rockstar>>, refresh: Boolean = false) {
        //get list from db
        val rockstars = if (!refresh) ArrayList(dao.getAll().map { it.toRockstar() }) else arrayListOf()
        Log.d("DEBUGLIST", "dao getAll $rockstars")
        if (rockstars.size > 0) {
            withContext(Dispatchers.Main) { list.value = rockstars }
        } else {
            //0 rockstar, make a http request
            RockstarApiClient().service.listRockstars().enqueue(object : Callback<ArrayList<Rockstar>> {
                override fun onFailure(call: Call<ArrayList<Rockstar>>, t: Throwable) {
                    Log.d("DEBUGLIST", "enqueue onFailure")
                }

                override fun onResponse(call: Call<ArrayList<Rockstar>>, response: Response<ArrayList<Rockstar>>) {
                    Log.d("DEBUGLIST", "enqueue onResponse ${response.body()}")
                    val listFromApi: List<RockstarEntity>? =
                        response.body()?.sortedBy { it.index }?.map { it.toRockstarEntity() }
                    GlobalScope.launch(Dispatchers.IO) {
                        val listFromDb = dao.getAll()
                        //insert only the rockstar absent from the db
                        listFromApi?.forEach { rockstarFromApi ->
                            val l = listFromDb.filter { it._id == rockstarFromApi._id }
                            if (l.isEmpty()) {
                                dao.insert(rockstarFromApi)
                            }
                        }
                        //refresh the mutable list with the new content of the database
                        withContext(Dispatchers.Main) { list.value = ArrayList(dao.getAll().map { it.toRockstar() }) }
                    }
                }
            })
        }
    }

    /**
     * Retrieves the bookmarks from the database
     * @param list
     */
    suspend fun loadBookmarks(list: MutableLiveData<ArrayList<Rockstar>>) {
        val bookmarks = dao.getAllBookmarks().map { it.toRockstar() } as ArrayList<Rockstar>
        Log.d("DEBUGLIST", "dao getBookmarks $bookmarks")
        withContext(Dispatchers.Main) { list.value = bookmarks }
    }

    /**
     * Set a rockstar as a favorite and save it in the database
     * @param list
     * @param index
     */
    suspend fun addToBookmark(list: MutableLiveData<ArrayList<Rockstar>>, index: Int) {
        val rockstars = list.value!!
        rockstars[index].bookmark = true
        dao.update(rockstars[index].toRockstarEntity())
    }

    /**
     * Set a rockstar as a non-favorite and save it in the database
     * @param list
     * @param index
     */
    suspend fun removeFromBookmark(list: MutableLiveData<ArrayList<Rockstar>>, index: Int) {
        val rockstars = list.value!!
        rockstars[index].bookmark = false
        dao.update(rockstars[index].toRockstarEntity())
    }


}

