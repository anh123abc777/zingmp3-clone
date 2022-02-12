package app.com.crawlmp3.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import app.com.crawlmp3.MainViewModel
import app.com.crawlmp3.MainViewModelFactory
import app.com.crawlmp3.databinding.FragmentPlaylistBinding
import app.com.crawlmp3.weeklyrankings.ListSongAdapter
import app.com.crawlmp3.weeklyrankings.OnClickListener
import timber.log.Timber


class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var idAlbum : String
    private lateinit var viewModel: PlaylistViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistBinding.inflate(inflater)

        if(arguments !=null){
            idAlbum = PlaylistFragmentArgs.fromBundle(requireArguments()).idAlbum
//            createEndWithHTMLForIdAlbum()
        }else {
            idAlbum = ""
        }

        val viewModelFactory = PlaylistViewModelFactory(idAlbum)
        viewModel = ViewModelProvider(this,viewModelFactory).get(PlaylistViewModel::class.java)
        binding.viewModel = viewModel

        val adapter = ListSongAdapter(OnClickListener { song ->
            viewModel.onSongClick(song)
        })
        binding.musicList.adapter = adapter

//        viewModel.selectedSong.observe(viewLifecycleOwner, Observer {
//            if(it!=null)
//            {
//                this.findNavController().navigate(PlaylistFragmentDirections.actionPlaylistFragmentToPlayViewFragment(it))
//                viewModel.doneSelect()
//            }
//        })

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(PlaylistFragmentDirections.actionPlaylistFragmentToHomeFragment())
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

//    private fun createEndWithHTMLForIdAlbum(){
//        idAlbum += ".html"
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = MainViewModelFactory(requireActivity().application)
        val mainViewModel = ViewModelProvider(requireActivity(),factory).get(MainViewModel::class.java)

        viewModel.navigateToSelectedSong.observe(viewLifecycleOwner) {
            if (it != null) {

                viewModel.songs.observe(viewLifecycleOwner) {
                    if (viewModel.songs.value != mainViewModel.songs.value)
                        try {
                            mainViewModel.setData(viewModel.songs.value!!)
                            Timber.i("complete send Data")
                        } catch (e: Exception) {
                            Timber.i(e.message)
                        }
                }

                mainViewModel.setSongSelected(it)
                viewModel.onPlayViewNavigated()
            }
        }

    }



}