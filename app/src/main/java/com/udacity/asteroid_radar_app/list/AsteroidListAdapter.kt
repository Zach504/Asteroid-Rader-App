package com.udacity.asteroid_radar_app.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroid_radar_app.data.Asteroid
import com.udacity.asteroid_radar_app.databinding.AsteroidListItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AsteroidListAdapter(val onClickListener: OnClickListener) : ListAdapter<Asteroid, AsteroidListAdapter.AsteroidViewHolder>(DiffCallback){

    //Inflate Asteroid Layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(AsteroidListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    //Reuse/Bind to Asteroid Layout
    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid)
        }
        holder.bind(asteroid)
    }

    //Just update the data object for the item binding and data binding will take care of the value updates
    class AsteroidViewHolder(private var binding: AsteroidListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid){
            binding.asteroid = asteroid
            //Forces data binding to execute immediately. Allows RecyclerView to make correct view size measurements
            binding.executePendingBindings()
        }
    }

    //Determine which items have changed upon data reload. Greatly improves efficiency when the backing data is updated.
    companion object DiffCallback: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem //3 equals checks for reference equality
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    //Custom click listener for the sake of passing asteroid to the details screen
    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

    //Submit list for update
    fun updateList(list: List<Asteroid>?){
        CoroutineScope(Dispatchers.Default).launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
    }

}