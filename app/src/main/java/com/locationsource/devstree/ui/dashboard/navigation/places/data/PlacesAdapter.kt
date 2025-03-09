package com.locationsource.devstree.ui.dashboard.navigation.places.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.locationsource.devstree.data.local.entity.Places
import com.locationsource.devstree.databinding.ItemPlacesBinding

class PlacesAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    private var placesList  = ArrayList<Places>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding =
            ItemPlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding = binding, listener)
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(place = placesList[position])

    }

    fun submitList(placeList: List<Places>) {
        placesList = placeList as ArrayList
        notifyDataSetChanged()
    }

    fun clearList() {
        placesList   = ArrayList<Places>()
        notifyDataSetChanged()
    }

    fun removePlace(position: Int){
        placesList.removeAt(position)
        notifyDataSetChanged()
    }

    class PlaceViewHolder(private val binding: ItemPlacesBinding,val listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Places) {
            binding.place = place
            binding.executePendingBindings()

            binding.imgEdit.setOnClickListener {
                listener.onItemClick(place)
            }

            binding.imgDelete.setOnClickListener {
                listener.onDeleteClick(place.id, bindingAdapterPosition)
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(place: Places)
        fun onDeleteClick(id: Int, position: Int)
    }

}