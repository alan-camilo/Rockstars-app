package fr.camilo.rockstarsapp

import android.app.Application
import fr.camilo.rockstarsapp.data.RockstarRepository
import fr.camilo.rockstarsapp.db.RockstarDatabase
import fr.camilo.rockstarsapp.db.dao.RockstarDao
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class ApplicationRockstar : Application(), KodeinAware {
    //Dependency injection configuration
    override val kodein by Kodein.lazy {
        bind<RockstarDao>() with singleton { RockstarDatabase.getDatabase(applicationContext).rockstarDao() }
        bind<RockstarRepository>() with singleton { RockstarRepository( instance() ) }
    }
}