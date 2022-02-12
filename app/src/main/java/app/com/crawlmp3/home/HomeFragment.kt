package app.com.crawlmp3.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import app.com.crawlmp3.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel : HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentHomeBinding.inflate(inflater)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this

        val adapter = HomeAdapter(OnClickListener {  idAlbum->
            homeViewModel.onClickAlbum(idAlbum)
        })
        binding.albums.adapter = adapter

        val adapterIndie = HomeAdapter(OnClickListener{ idAlbum ->
            homeViewModel.onClickAlbum(idAlbum)
        })
        binding.albumsIndie.adapter = adapterIndie

        observeNavigateToPlaylist()

        return binding.root
    }

    private fun observeNavigateToPlaylist(){
        homeViewModel.navigateToTracksOfAlbum.observe(viewLifecycleOwner) {
            if (it != "") {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToPlaylistFragment(it))
                homeViewModel.doneNavigatedToTracksOfAlbum()
            }
        }
    }

}