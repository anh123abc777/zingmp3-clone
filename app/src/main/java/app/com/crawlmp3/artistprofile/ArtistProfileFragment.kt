package app.com.crawlmp3.artistprofile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import app.com.crawlmp3.MainViewModel
import app.com.crawlmp3.MainViewModelFactory
import app.com.crawlmp3.databinding.FragmentArtistProfileBinding
import app.com.crawlmp3.weeklyrankings.ListSongAdapter
import app.com.crawlmp3.weeklyrankings.OnClickListener
import timber.log.Timber


class ArtistProfileFragment : Fragment() {

    private lateinit var binding : FragmentArtistProfileBinding
    private lateinit var viewModel: ArtistProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentArtistProfileBinding.inflate(inflater)
        val artist = ArtistProfileFragmentArgs.fromBundle(requireArguments()).artistProfile
        val factory = ArtistProfileViewModelFactory(artist)
        viewModel = ViewModelProvider(this,factory).get(ArtistProfileViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = ListSongAdapter(OnClickListener {
            viewModel.onSongClick(it)
        })
        binding.songGroup.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = MainViewModelFactory(requireActivity().application)
        val mainViewModel = ViewModelProvider(requireActivity(),factory).get(MainViewModel::class.java)

        viewModel.songSelected.observe(viewLifecycleOwner) {
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
                viewModel.donePlayedSong()
            }
        }
    }
}