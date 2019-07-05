package fr.camilo.rockstarsapp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import fr.camilo.rockstarsapp.R
import fr.camilo.rockstarsapp.fragment.RockstarListFragment
import fr.camilo.rockstarsapp.util.ACTIVITY_TYPE
import fr.camilo.rockstarsapp.util.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigator.*


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString(ACTIVITY_TYPE, Constants.MAIN_ACTIVITY.value)
        fragment = RockstarListFragment()
        fragment.arguments = bundle
        fragmentTransaction.add(R.id.list, fragment)
        fragmentTransaction.commit()


        bookmarks_btn.setOnClickListener {
            val intent = Intent(this, BookmarksActivity::class.java)
            startActivity(intent)
        }

        profile_btn.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        swiperefresh.setOnRefreshListener(this)

        val searchWidget = search_view
        searchWidget.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                (fragment as RockstarListFragment).filterList(p0 ?: "")
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                (fragment as RockstarListFragment).filterList(p0 ?: "")
                return true
            }

        })
    }

    override fun onRefresh() {
        (fragment as RockstarListFragment).refreshList { swiperefresh.isRefreshing = false }
    }
}
