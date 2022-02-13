package app.com.crawlmp3.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.crawlmp3.MainViewModel
import app.com.crawlmp3.MainViewModelFactory
import app.com.crawlmp3.R
import app.com.crawlmp3.adapter.ArtistAdapter
import app.com.crawlmp3.databinding.FragmentSearchBinding
import app.com.crawlmp3.home.PlaylistAdapter
import app.com.crawlmp3.network.Song
import app.com.crawlmp3.weeklyrankings.ListSongAdapter
import app.com.crawlmp3.weeklyrankings.OnClickListener
import timber.log.Timber


class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentSearchBinding.inflate(inflater)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setAdapterSongs()

        setAdapterAlbums()

        setAdapterArtists()

        observeNavigateToArtistFragment()

        setUpSearchBar()

        setUpChip()

        return binding.root
    }

    private fun setAdapterSongs(){
        val adapterSongs = ListSongAdapter(OnClickListener {
            viewModel.onSongClick(it)
        })
        binding.songGroup.adapter = adapterSongs
    }

    private fun setAdapterAlbums(){
        val adapterAlbums = PlaylistAdapter(app.com.crawlmp3.home.OnClickListener {
            viewModel.onClickAlbum(it)
        })
        viewModel.navigateToTracksOfAlbum.observe(viewLifecycleOwner){
            if(it!="") {
                findNavController().navigate(
                    SearchFragmentDirections.actionSearchFragmentToPlaylistFragment(it)
                )
                viewModel.doneNavigatedToTracksOfAlbum()
            }
        }

        binding.albumGroup.adapter = adapterAlbums
    }

    private fun setAdapterArtists(){
        val adapterArtists = ArtistAdapter(app.com.crawlmp3.adapter.OnClickListener {
            viewModel.onClickArtist(it)
        })

        binding.artistGroup.adapter = adapterArtists
    }

    private fun observeNavigateToArtistFragment(){
        viewModel.navigateArtist.observe(viewLifecycleOwner){
            if(it!=null) {
                findNavController().navigate(
                    SearchFragmentDirections
                        .actionSearchFragmentToArtistProfileFragment(it)
                )
                viewModel.doneNavigatedToArtistProfile()
            }
        }
    }

    private fun setUpSearchBar(){
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.setQuery(query!!)
                viewModel.onQueryChanged()
                Timber.i("query ${viewModel.query.value}")
                binding.artistGroup.visibility = View.VISIBLE
                binding.txtArtist.visibility = View.VISIBLE

                binding.albumGroup.visibility = View.VISIBLE
                binding.txtAlbum.visibility = View.VISIBLE

                binding.songGroup.visibility = View.VISIBLE
                binding.txtSong.visibility = View.VISIBLE

                binding.chips.visibility = View.VISIBLE

                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun setUpChip(){
        binding.filters.filterGroup.setOnCheckedChangeListener { group, checkedId ->
            when(group.checkedChipId){
                R.id.song -> {  binding.artistGroup.visibility = View.GONE
                    binding.txtArtist.visibility = View.GONE

                    binding.albumGroup.visibility = View.GONE
                    binding.txtAlbum.visibility = View.GONE

                    binding.songGroup.visibility = View.VISIBLE
                    binding.txtSong.visibility = View.VISIBLE
                    binding.songGroup.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                }
                R.id.album -> { binding.artistGroup.visibility = View.GONE
                    binding.txtArtist.visibility =View.GONE

                    binding.albumGroup.visibility = View.VISIBLE
                    binding.txtAlbum.visibility = View.VISIBLE
                    binding.albumGroup.layoutManager=
                        GridLayoutManager(context,
                            2,GridLayoutManager.VERTICAL,false)


                    binding.songGroup.visibility = View.GONE
                    binding.txtSong.visibility = View.GONE
                }
                R.id.artist -> {binding.artistGroup.visibility = View.VISIBLE
                    binding.txtArtist.visibility = View.VISIBLE
                    binding.artistGroup.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

                    binding.albumGroup.visibility = View.GONE
                    binding.txtAlbum.visibility = View.GONE

                    binding.songGroup.visibility = View.GONE
                    binding.txtSong.visibility = View.GONE
                }

                else -> {

                    binding.artistGroup.visibility = View.VISIBLE
                    binding.txtArtist.visibility = View.VISIBLE
                    binding.artistGroup.layoutParams.height = 620


                    binding.albumGroup.visibility = View.VISIBLE
                    binding.txtAlbum.visibility = View.VISIBLE
                    binding.albumGroup.layoutManager=
                        LinearLayoutManager(context,
                            LinearLayoutManager.HORIZONTAL,false)

                    binding.songGroup.visibility = View.VISIBLE
                    binding.txtSong.visibility = View.VISIBLE
                    binding.songGroup.layoutParams.height = 620

                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = MainViewModelFactory(requireActivity().application)
        val mainViewModel = ViewModelProvider(requireActivity(),factory).get(MainViewModel::class.java)

        viewModel.songSelected.observe(viewLifecycleOwner) {
            if (it != null) {

                viewModel.rawSearch.observe(viewLifecycleOwner) { rawSearch ->

                    var songs = listOf<Song>()
                    rawSearch.data?.map { data ->
                        if (data.song!=null) {
                            songs = data.song
                        }
                    }

                    if (songs!= mainViewModel.songs.value)
                        try {
                            mainViewModel.setData(songs)

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