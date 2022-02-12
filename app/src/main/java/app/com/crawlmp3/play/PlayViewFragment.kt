package app.com.crawlmp3.play

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import app.com.crawlmp3.MainViewModel
import app.com.crawlmp3.MainViewModelFactory
import app.com.crawlmp3.R
import app.com.crawlmp3.databinding.FragmentPlayViewBinding
import timber.log.Timber

class PlayViewFragment : Fragment() {

    private lateinit var binding : FragmentPlayViewBinding
    private lateinit var viewModel: PlayViewViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentPlayViewBinding.inflate(inflater)
        viewModel =  ViewModelProvider(this).get(PlayViewViewModel::class.java)
        binding.lifecycleOwner = this
        onSeekBarChangeListener()

        return binding.root
    }

    private fun onSeekBarChangeListener(){
        binding.simpleSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) {
                    viewModel.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = MainViewModelFactory(requireActivity().application)
        mainViewModel = ViewModelProvider(requireActivity(),factory).get(MainViewModel::class.java)

        /*
        lỗi chuỳn lin tục và lin tục bài hát :v .. giữ lại nha.. luv
        nền tản hôi nác
        /var songSelectedInPlay : Song?=null
        */
        transferSongSelected()
        setupBackgroundButtonPlay()
        setFunctionOfButton()

    }

    private fun transferSongSelected(){
        mainViewModel.songSelected.observe(viewLifecycleOwner) {
            viewModel.setData(it)
            Timber.i("${mainViewModel.songSelected.value}")
            setupInterface()
            setupPlayer()
            /*
            setupBackgroundButtonPlay()
            setFunctionOfButton()
             */
        }
    }

    private fun setupInterface(){
        binding.viewModel = viewModel
    }

    private fun setupPlayer(){
        viewModel.exoPlayer = mainViewModel.exoPlayer
        viewModel.initSeekBar()
    }

    private fun setupBackgroundButtonPlay(){
        mainViewModel.stateExoPlayer.observe(requireActivity()){
            if(it){
                binding.play.setBackgroundResource(R.drawable.pause)
                viewModel.setStatePlaying()
            }else{
                binding.play.setBackgroundResource(R.drawable.play)
                viewModel.setStateOnPause()
            }
        }
    }

    private fun setFunctionOfButton(){
        viewModel.action.observe(viewLifecycleOwner) { act ->
            if (act != ACTIONS.ONPAUSE && act != ACTIONS.PLAYING) {
                when (act) {
                    ACTIONS.PAUSE -> mainViewModel.onChangeStatePlay()

                    ACTIONS.PLAY -> mainViewModel.onChangeStatePlay()

                    ACTIONS.PREVIOUS -> mainViewModel.onPrevious()

                    ACTIONS.NEXT -> mainViewModel.onNext()

                    else -> Timber.i("Unknown Action")
                }
                viewModel.doneAction()
            }
        }
    }
}