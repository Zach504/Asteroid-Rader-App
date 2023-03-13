package com.udacity.asteroid_radar_app.detail

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.udacity.asteroid_radar_app.R
import com.udacity.asteroid_radar_app.databinding.FragmentDetailBinding

/**
 * Asteroid Detail Screen
 */
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).asteroid

        viewModel = DetailViewModel(asteroid)

        binding.viewModel = viewModel

        //Help button click listener and alert dialog creation
        binding.helpButton.setOnClickListener{
            AlertDialog.Builder(requireActivity()).setMessage(getString(R.string.astronomica_unit_explanation)).setPositiveButton(android.R.string.ok, null).create().show()
        }

        return binding.root
    }
}