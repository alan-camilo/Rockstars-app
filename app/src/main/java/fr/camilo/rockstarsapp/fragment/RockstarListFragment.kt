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
import fr.camilo.rockstarsapp.util.ACTIVITY_TYPE
import fr.camilo.rockstarsapp.util.Constants
import fr.camilo.rockstarsapp.viewmodel.RockstarsViewModel
import kotlinx.android.synthetic.main.rockstar_row.*

class RockstarListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var myDataset = arrayListOf<Rockstar>()
    private var myDatasetBak = arrayListOf<Rockstar>()
    private lateinit var model: RockstarsViewModel
    //action for the bookmark star button
    private val bookmarkAction = { isEnabled: Boolean, index: Int ->
        if (isEnabled) model.addToBookmark(myDataset[index].index) else model.removeFromBookmark(myDataset[index].index)
        Unit
    }
    //action for the bookmark delete button
    private val deleteAction = { _: Boolean, index: Int ->
        model.removeFromBookmark(myDataset[index].index)
        myDataset.remove(myDataset[index])
        myDatasetBak.remove(myDatasetBak[index])
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
        //get the name of the activity, important for the fragment behaviour
        val activityType = arguments!!.getString(ACTIVITY_TYPE)
        Log.d("DEBUG_LIST", "$activityType")

        viewManager = LinearLayoutManager(activity)

        //instantiates the RecyclerView Adapter
        viewAdapter = when (activityType) {
            Constants.MAIN_ACTIVITY.value -> RockstarsAdapter(myDataset, Constants.MAIN_ACTIVITY, bookmarkAction)
            Constants.BOOKMARKS_ACTIVITY.value -> RockstarsAdapter(
                myDataset,
                Constants.BOOKMARKS_ACTIVITY,
                deleteAction
            )
            else -> RockstarsAdapter(myDataset, Constants.MAIN_ACTIVITY, bookmarkAction)
        }
        recyclerView = activity!!.findViewById<RecyclerView>(R.id.recycler_view).apply {
            // use a linear layout manager
            layoutManager = viewManager
            // specify a viewAdapter
            adapter = viewAdapter
        }

        //instantiates the ViewModel and populates the dataset
        model = ViewModelProviders.of(this).get(RockstarsViewModel::class.java)
        when (activityType) {
            Constants.MAIN_ACTIVITY.value -> {
                model.getRockstars().observe(this, Observer<List<Rockstar>> {
                    Log.d("DEBUGLIST", "observer called $it")
                    myDataset.addAll(it)
                    myDatasetBak.addAll(it)
                    recyclerView.adapter!!.notifyDataSetChanged()
                })
            }
            Constants.BOOKMARKS_ACTIVITY.value -> {
                model.getBookmarks().observe(this, Observer<List<Rockstar>> {
                    Log.d("DEBUGLIST", "observer called $it")
                    myDataset.addAll(it)
                    myDatasetBak.addAll(it)
                    recyclerView.adapter!!.notifyDataSetChanged()
                })
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

    /**
     * Refresh the list making a new http request
     * @param callback
     */
    fun refreshList(callback: () -> Unit) {
        myDataset.clear()
        myDatasetBak.clear()
        recyclerView.adapter!!.notifyDataSetChanged()
        model.getRockstars(true).observe(this, Observer<List<Rockstar>> {
            Log.d("DEBUGLIST", "refresh observer called $it")
            myDataset.addAll(it)
            myDatasetBak.addAll(it)
            recyclerView.adapter!!.notifyDataSetChanged()
            callback()
        })
    }

    /**
     * Filter the list when searching a rockstar
     */
    fun filterList(str: String) {
        myDataset.clear()
        if (str != "") {
            myDataset.addAll(myDatasetBak.filter { it.name.toLowerCase().startsWith(str) })
        } else {
            myDataset.addAll(myDatasetBak)
        }
        recyclerView.adapter!!.notifyDataSetChanged()
    }
}