package dk.arongk.and1_recipeapp.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.arongk.and1_recipeapp.R
import dk.arongk.and1_recipeapp.data.model.recipe.RecipeDto

class RecipeAdapter(var recipes : List<RecipeDto>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.fragment_search_list_item, parent, false)
        return ViewHolder(view  )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageURI(Uri.parse(recipes.get(position).imageUrl))
        holder.title.setText(recipes.get(position).title)
        holder.workTime.setText(recipes.get(position).workTime)
        holder.totalTime.setText(recipes.get(position).totalTime)
        holder.servings.setText(recipes.get(position).servings)
        holder.description.setText(recipes.get(position).description)
    }

    override fun getItemCount() = recipes.size

    fun setData(newData : List<RecipeDto>){
        recipes = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       var image : ImageView = itemView.findViewById(R.id.sli_image)
       var title : TextView = itemView.findViewById(R.id.sli_title)
       var workTime : TextView = itemView.findViewById(R.id.sli_workTime)
       var totalTime : TextView= itemView.findViewById(R.id.sli_totalTime)
       var servings : TextView= itemView.findViewById(R.id.sli_servings)
       var description : TextView= itemView.findViewById(R.id.sli_description)
    }

}