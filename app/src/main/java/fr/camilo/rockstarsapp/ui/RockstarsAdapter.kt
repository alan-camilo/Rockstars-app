package fr.camilo.rockstarsapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.camilo.rockstarsapp.R
import fr.camilo.rockstarsapp.model.Rockstar
import fr.camilo.rockstarsapp.util.Constants

class RockstarsAdapter(
    private val myDataset: ArrayList<Rockstar>,
    val activityType: Constants,
    val onClick: (isEnabled: Boolean, index: Int) -> Unit
) :
    RecyclerView.Adapter<RockstarsAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    class MyViewHolder(linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout) {
        val tv_name: TextView = linearLayout.findViewById(R.id.rockstar_name)
        val tv_status: TextView = linearLayout.findViewById(R.id.rockstar_status)
        val iv_picture: ImageView = linearLayout.findViewById(R.id.rockstar_img)
        val action_btn: ImageButton = linearLayout.findViewById(R.id.action_btn)
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        // create a new view
        val linearLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.rockstar_row, parent, false) as LinearLayout
        return MyViewHolder(linearLayout)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var isEnabled = myDataset[position].bookmark ?: false
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tv_name.text = myDataset[position].name
        holder.tv_status.text = myDataset[position].about
        //change the icon onClick (full star or border star)
        holder.action_btn.setOnClickListener {
            isEnabled = !isEnabled
            onClick(isEnabled, holder.layoutPosition)
            if (activityType == Constants.MAIN_ACTIVITY) {
                if (isEnabled) {
                    holder.action_btn.setImageResource(R.mipmap.baseline_star_black_36)
                } else {
                    holder.action_btn.setImageResource(R.mipmap.baseline_star_border_black_36)
                }
            }
        }
        //change the icon according to the activity (trash or star)
        when (activityType) {
            Constants.MAIN_ACTIVITY -> {
                val isBookmark = myDataset[position].bookmark ?: false
                if (isBookmark) {
                    holder.action_btn.setImageResource(R.mipmap.baseline_star_black_36)
                } else {
                    holder.action_btn.setImageResource(R.mipmap.baseline_star_border_black_36)
                }
            }
            Constants.BOOKMARKS_ACTIVITY -> holder.action_btn.setImageResource(R.mipmap.baseline_delete_outline_black_36)
        }
        //load the picture of the rockstar
        Picasso.get().load(myDataset[position].picture).into(holder.iv_picture)
    }

    override fun getItemCount() = myDataset.size
}
