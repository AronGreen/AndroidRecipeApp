package dk.arongk.and1_recipeapp.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.arongk.and1_recipeapp.R
import dk.arongk.and1_recipeapp.models.recipe.RecipeWithIngredientsDto

class RecipeAdapter(
    context: Context,
    private val mOnListItemClickListener: OnListItemClickListener
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var recipes: List<RecipeWithIngredientsDto>

    init {
        recipes = listOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.fragment_search_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageURI(Uri.parse(recipes[position].imageUrl))
        holder.title.text = recipes[position].title
        holder.workTime.text = recipes[position].workTime.toString()
        holder.totalTime.text = recipes[position].totalTime.toString()
        holder.servings.text = recipes[position].servings.toString()
        val calories =  recipes[position].ingredients.sumBy {
            it.calories.toIntOrNull() ?: 0
        }
        holder.calories.text = "$calories  calories"
    }

    override fun getItemCount() = recipes.size

    fun setData(newData: List<RecipeWithIngredientsDto>) {
        recipes = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        var image: ImageView = itemView.findViewById(R.id.sli_image)
        var title: TextView = itemView.findViewById(R.id.sli_title)
        var workTime: TextView = itemView.findViewById(R.id.sli_workTime)
        var totalTime: TextView = itemView.findViewById(R.id.sli_totalTime)
        var servings: TextView = itemView.findViewById(R.id.sli_servings)
        var calories: TextView = itemView.findViewById(R.id.sli_calories)

        override fun onClick(v: View?) {
            mOnListItemClickListener.onListItemClick(adapterPosition)
        }
    }

    interface OnListItemClickListener {
        fun onListItemClick(clickedItemIndex: Int)
    }
}