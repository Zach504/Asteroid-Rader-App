package com.udacity.asteroid_radar_app.list

import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.udacity.asteroid_radar_app.R
import com.udacity.asteroid_radar_app.data.Asteroid
import com.udacity.asteroid_radar_app.data.AsteroidDatabase
import com.udacity.asteroid_radar_app.data.AsteroidDatabaseDao
import com.udacity.asteroid_radar_app.databinding.FragmentListBinding
import com.udacity.asteroid_radar_app.main.MainViewModel
import com.udacity.asteroid_radar_app.main.MainViewModelFactory


/**
 * Asteroid List Screen
 */
class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    private lateinit var observer: Observer<List<Asteroid>>

    val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(AsteroidDatabase.getInstance(requireActivity().application).asteroidDatabaseDao,requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        //Set up adapter and add handling for list item selection
        val adapter = AsteroidListAdapter(AsteroidListAdapter.OnClickListener {
            viewModel.displayAsteroidDetails(it) })
        binding.asteroidRecyclerView.adapter = adapter
        observer =  Observer {
            it?.let{
                adapter.updateList(it)
            }
        }
        viewModel.asteroids.observe(viewLifecycleOwner, observer)

        viewModel.selectedAsteroid.observe(viewLifecycleOwner, Observer {
            if(null != it){
                this.findNavController().navigate(ListFragmentDirections.actionListFragmentToDetailFragment(it))
                viewModel.displayAsteroidDetailsComplete()
            }
        })

        //Set up picture of the day
        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            if(null != it){
                val imageUri = it.url.toUri()
                binding.imageOfTheDayText.text = it.title
                binding.imageOfTheDayImage.contentDescription = getString(R.string.nasa_s_picture_of_the_day_with_title,it.title)
                Glide.with(binding.imageOfTheDayImage.context).load(imageUri).apply(RequestOptions().placeholder(R.drawable.placeholder_picture_of_day)).into(binding.imageOfTheDayImage)
            }
        })

        setupMenu()

        return binding.root
    }

    fun setupMenu(){
        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }
            //TODO: Ask instructors / reviewers if there is a better way to handle updating livedata from new queries to Room DB. A post filter sounds too dirty implementation wise.
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.show_all-> {
                        viewModel.asteroids.removeObserver(observer)
                        viewModel.showAllAsteroids()
                        viewModel.asteroids.observe(viewLifecycleOwner, observer)
                    }
                    R.id.show_week -> {
                        viewModel.asteroids.removeObserver(observer)
                        viewModel.showWeeksAsteroids()
                        viewModel.asteroids.observe(viewLifecycleOwner, observer)
                    }
                    R.id.show_day -> {
                        viewModel.asteroids.removeObserver(observer)
                        viewModel.showDaysAsteroids()
                        viewModel.asteroids.observe(viewLifecycleOwner, observer)
                    }
                    else -> return false
                }
                return true
            }

        }, viewLifecycleOwner)
    }
}
