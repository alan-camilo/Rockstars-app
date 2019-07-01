package fr.camilo.rockstarsapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import fr.camilo.rockstarsapp.R
import kotlinx.android.synthetic.main.navigator.*


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        bookmarks_btn.setOnClickListener {
            val intent = Intent(this, BookmarksActivity::class.java)
            startActivity(intent)
        }

        rockstars_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_save -> Unit //TODO
        }
        return super.onOptionsItemSelected(item)
    }
}
