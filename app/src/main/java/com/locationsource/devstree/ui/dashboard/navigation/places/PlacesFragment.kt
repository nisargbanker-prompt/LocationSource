package com.locationsource.devstree.ui.dashboard.navigation.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.locationsource.devstree.R
import com.locationsource.devstree.data.local.entity.Places
import com.locationsource.devstree.databinding.FragmentAllPlacesBinding
import com.locationsource.devstree.ui.dashboard.navigation.places.bottomsheet.BottomSheetFragment
import com.locationsource.devstree.ui.dashboard.navigation.places.data.PlacesAdapter
import com.locationsource.devstree.ui.dashboard.navigation.places.data.PlacesViewModel
import com.locationsource.devstree.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesFragment : Fragment(), PlacesAdapter.OnItemClickListener {

    lateinit var binding: FragmentAllPlacesBinding
    private val viewModel: PlacesViewModel by viewModels()
    private lateinit var adapter: PlacesAdapter
    private var placesList = ArrayList<Places>()
    private var selectedSortMethod = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllPlacesBinding.inflate(layoutInflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setAdapter()
        getDataFromDB()
        menuClickListner()
        getDataFromBottomSheet()

        return binding.root
    }

    private fun setAdapter() {
        adapter = PlacesAdapter(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getDataFromBottomSheet() {
        childFragmentManager.setFragmentResultListener(
            Constants.DATA, this
        ) { _, bundle ->
            val result = bundle.getInt(Constants.RESULT)
            if (result == 1) {
                selectedSortMethod = 1
                adapter.clearList()
                placesList = viewModel.getAllPalcesAsc() as ArrayList
                adapter.submitList(placesList)
            }else {
                selectedSortMethod = 2
                adapter.clearList()
                placesList = viewModel.getAllPalcesDesc() as ArrayList
                adapter.submitList(placesList)
            }
        }
    }

    private fun menuClickListner() {
        binding.materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_sort) {
                val bottomSheetFragment = BottomSheetFragment()
                val bundle = Bundle()
                bundle.putInt(Constants.SORT, selectedSortMethod)
                bottomSheetFragment.arguments = bundle
                bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
    }

    private fun getDataFromDB() {
        adapter.clearList()

        var data = mutableListOf<Places>()

        if (selectedSortMethod == 0){
            data = viewModel.getAllPalces() as ArrayList
        }else if(selectedSortMethod == 1){
            data = viewModel.getAllPalcesAsc() as ArrayList
        }else {
            data = viewModel.getAllPalcesDesc() as ArrayList
        }

        if (data.isEmpty()) {
            placesList = ArrayList()
        } else {

            if (data.size > 1){
                binding.materialToolbar.inflateMenu(R.menu.sort_menu)
                binding.fabNavigation.visibility = View.VISIBLE
            }

            binding.constraintNoData.visibility = View.GONE
            placesList = data
            adapter.submitList(placesList)
        }
    }

    override fun onItemClick(place: Places) {
        val bundle = Bundle()
        bundle.putString(Constants.LAT, place.lat!!)
        bundle.putString(Constants.LNG, place.lng!!)
        bundle.putString(Constants.NAME, place.cityName)
        bundle.putInt(Constants.ID, place.id)
        findNavController().navigate(R.id.action_allPlacesFragment_to_addPlacesFragment, bundle)
    }

    override fun onDeleteClick(id: Int, position: Int) {
        android.app.AlertDialog.Builder(this.requireActivity()).setTitle(getString(R.string.delete_place))
            .setMessage(getString(R.string.delete_place_description)).setCancelable(false)
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                viewModel.deletePlace(id)
                adapter.removePlace(position)
                dialog.dismiss()

            }.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


}