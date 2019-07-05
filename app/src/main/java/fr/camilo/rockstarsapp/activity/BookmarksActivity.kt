package fr.camilo.rockstarsapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fr.camilo.rockstarsapp.R
import fr.camilo.rockstarsapp.fragment.RockstarListFragment
import fr.camilo.rockstarsapp.util.ACTIVITY_TYPE
import fr.camilo.rockstarsapp.util.Constants
import kotlinx.android.synthetic.main.navigator.*

class BookmarksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmarks)

        val bundle = Bundle()
        bundle.putString(ACTIVITY_TYPE, Constants.BOOKMARKS_ACTIVITY.value)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = RockstarListFragment()
        fragment.arguments = bundle
        fragmentTransaction.add(R.id.list, fragment)
        fragmentTransaction.commit()

        profile_btn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        rockstars_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
