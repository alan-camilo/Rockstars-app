package fr.camilo.rockstarsapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import fr.camilo.rockstarsapp.R
import fr.camilo.rockstarsapp.model.Rockstar
import fr.camilo.rockstarsapp.ui.RockstarsAdapter
import fr.camilo.rockstarsapp.util.Constants
import fr.camilo.rockstarsapp.viewmodel.RockstarsViewModel
import kotlinx.android.synthetic.main.rockstar_row.*

class RockstarListFragment(val activityType: Constants) : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var myDataset = arrayListOf<Rockstar>()
    private lateinit var model: RockstarsViewModel
    private var swiperefresh: SwipeRefreshLayout? = null
    private val bookmarkAction = { isEnabled: Boolean, index: Int ->
        if (isEnabled) model.addToBookmark(index) else model.removeFromBookmark(index)
        Unit
    }
    private val deleteAction = { _: Boolean, index: Int ->
        model.removeFromBookmark(myDataset[index].index)
        myDataset.remove(myDataset[index])
        recyclerView.adapter!!.notifyDataSetChanged()
        Unit
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        //setContentView(R.layout.fragment_list)
        swiperefresh = activity?.findViewById(R.id.swiperefresh)

        viewManager = LinearLayoutManager(activity)

        viewAdapter = when (activityType) {
            Constants.MAIN_ACTIVITY -> RockstarsAdapter(myDataset, activityType, bookmarkAction)
            Constants.BOOKMARKS_ACTIVITY -> RockstarsAdapter(myDataset, activityType, deleteAction)
        }
        recyclerView = activity!!.findViewById<RecyclerView>(R.id.recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            //setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        model = ViewModelProviders.of(this).get(RockstarsViewModel::class.java)
        when (activityType) {
            Constants.MAIN_ACTIVITY -> {
                model.getRockstars().observe(this, Observer<List<Rockstar>> {
                    Log.d("DEBUGLIST", "observer called $it")
                    myDataset.addAll(it)
                    recyclerView.adapter!!.notifyDataSetChanged()
                })
            }
            Constants.BOOKMARKS_ACTIVITY -> {
                model.getBookmarks().observe(this, Observer<List<Rockstar>> {
                    Log.d("DEBUGLIST", "observer called $it")
                    myDataset.addAll(it)
                    recyclerView.adapter!!.notifyDataSetChanged()
                })
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

    private fun setUpBookmarkBtn(isEnabled: Boolean) {
        action_btn.setOnClickListener {
            Toast.makeText(activity, "fav! $isEnabled", Toast.LENGTH_LONG).show()
        }
    }

    fun refreshList(callback: () -> Unit) {
        myDataset.clear()
        recyclerView.adapter!!.notifyDataSetChanged()
        model.getRockstars(true).observe(this, Observer<List<Rockstar>> {
            Log.d("DEBUGLIST", "refresh observer called $it")
            myDataset.addAll(it)
            recyclerView.adapter!!.notifyDataSetChanged()
            callback()
        })
    }
}