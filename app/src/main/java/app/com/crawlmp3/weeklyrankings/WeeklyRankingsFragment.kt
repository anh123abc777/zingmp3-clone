package app.com.crawlmp3.weeklyrankings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.crawlmp3.MainViewModel
import app.com.crawlmp3.MainViewModelFactory
import app.com.crawlmp3.R
import app.com.crawlmp3.adapter.GroupPlaylistAdapter
import app.com.crawlmp3.databinding.FragmentWeeklyRankingsBinding
import app.com.crawlmp3.home.HomeFragmentDirections
import timber.log.Timber

class WeeklyRankingsFragment : Fragment() {

    private lateinit var viewModel: WeeklyRankingsViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentWeeklyRankingsBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(WeeklyRankingsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        val adapter = GroupPlaylistAdapter(OnClickListener {
//            viewModel.onClickAlbum(it)
//        })
//        binding.groupPlaylist.adapter = adapter
        val adapter = ListSongAdapter(OnClickListener { song ->
            viewModel.onSongClick(song)
        })
        binding.musicList.adapter = adapter

        observeNavigateToPlaylist()

        return binding.root
    }

    private fun observeNavigateToPlaylist(){
        viewModel.navigateToTracksOfAlbum.observe(viewLifecycleOwner) {
            if (it != "") {
                this.findNavController()
                    .navigate(WeeklyRankingsFragmentDirections.actionWeeklyRankingsFragmentToPlaylistFragment(it))
                viewModel.doneNavigatedToTracksOfAlbum()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createMainViewModel()
        changeSongSelected()
    }

    private fun createMainViewModel(){
        val factory = MainViewModelFactory(requireActivity().application)
        mainViewModel = ViewModelProvider(requireActivity(),factory).get(MainViewModel::class.java)
    }

    private fun changeSongSelected(){
        viewModel.selectedSong.observe(viewLifecycleOwner) {
            if (it != null) {
                changeMainData()
                mainViewModel.setSongSelected(it)
                Timber.i("send complete songSelected")
                viewModel.doneSelect()
            }
        }
    }

    private fun changeMainData(){
        viewModel.songs.observe(viewLifecycleOwner) {
            if (viewModel.songs.value != mainViewModel.songs.value) {
                mainViewModel.setData(viewModel.songs.value!!)
                Timber.i("send complete Data")
            }
        }
    }
}