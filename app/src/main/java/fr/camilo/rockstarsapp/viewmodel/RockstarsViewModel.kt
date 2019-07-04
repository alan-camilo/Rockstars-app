package fr.camilo.rockstarsapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fr.camilo.rockstarsapp.data.RockstarRepository
import fr.camilo.rockstarsapp.model.Rockstar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class RockstarsViewModel(app: Application) : AndroidViewModel(app), KodeinAware {
    override val kodein by kodein(getApplication() as Context)

    private val repository by instance<RockstarRepository>()

    private var rockstars: MutableLiveData<ArrayList<Rockstar>> =
        MutableLiveData<ArrayList<Rockstar>>().also {
            viewModelScope.launch(Dispatchers.IO) { repository.loadRockstars(it) }
        }

    private var bookmarks: MutableLiveData<ArrayList<Rockstar>> = MutableLiveData<ArrayList<Rockstar>>().also {
        viewModelScope.launch(Dispatchers.IO) { repository.loadBookmarks(it) }
    }

    fun getRockstars(refresh: Boolean = false): LiveData<ArrayList<Rockstar>> {
        if (refresh) {
            rockstars = MutableLiveData<ArrayList<Rockstar>>().also {
                viewModelScope.launch(Dispatchers.IO) { repository.loadRockstars(it, true) }
            }
        }
        return rockstars
    }

    fun getBookmarks(): LiveData<ArrayList<Rockstar>> = bookmarks

    fun addToBookmark(index: Int) = viewModelScope.launch(Dispatchers.IO) { repository.addToBookmark(rockstars, index) }

    fun removeFromBookmark(index: Int) =
        viewModelScope.launch(Dispatchers.IO) { repository.removeFromBookmark(rockstars, index) }
}