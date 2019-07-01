package fr.camilo.rockstarsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.camilo.rockstarsapp.data.RockstarRepository
import fr.camilo.rockstarsapp.model.Rockstar

class RockstarsViewModel(app: Application) : AndroidViewModel(app) {
    val repository = RockstarRepository()
    private val rockstars: MutableLiveData<ArrayList<Rockstar>> by lazy {
        MutableLiveData<ArrayList<Rockstar>>().also {
            repository.loadRockstars(it)
        }
    }

    fun getRockstars(): LiveData<ArrayList<Rockstar>> {
        return rockstars
    }
}