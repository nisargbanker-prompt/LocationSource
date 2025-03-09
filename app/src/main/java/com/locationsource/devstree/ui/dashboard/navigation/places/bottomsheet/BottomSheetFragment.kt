package com.locationsource.devstree.ui.dashboard.navigation.places.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.locationsource.devstree.databinding.FragmentBottomSheetBinding
import com.locationsource.devstree.utils.Constants

class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetBinding.inflate(layoutInflater)

        val selectedSort = this.requireArguments().getInt(Constants.SORT)

        if (selectedSort != 0) {
            if (selectedSort == 1) {
                binding.radioAscending.isChecked = true
            } else {
                binding.radioDescending.isChecked = true
            }
        }

        binding.btnApply.setOnClickListener {
            val selectedId = binding.radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                if (binding.radioAscending.isChecked) {
                    setFragmentResult(Constants.DATA, Bundle().apply {
                        putInt(Constants.RESULT, 1)
                    })
                } else {
                    setFragmentResult(Constants.DATA, Bundle().apply {
                        putInt(Constants.RESULT, 2)
                    })
                }
            }
            dismiss()
        }

        return binding.root
    }
}