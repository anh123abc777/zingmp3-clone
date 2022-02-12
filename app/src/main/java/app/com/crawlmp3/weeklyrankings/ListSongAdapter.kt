package app.com.crawlmp3.weeklyrankings


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.com.crawlmp3.databinding.SongElementViewBinding
import app.com.crawlmp3.network.Song

class ListSongAdapter(val onClickListener : OnClickListener) : ListAdapter<Song, ListSongAdapter.ViewHolder>(DiffCallback) {
    private companion object DiffCallback : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

    }

    class ViewHolder private constructor(private val binding : SongElementViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(song: Song, onClickListener: OnClickListener){
            binding.song = song
            binding.onListener = onClickListener
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SongElementViewBinding.inflate(layoutInflater,parent,false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = getItem(position)
        holder.bind(song,onClickListener)
    }

}
class OnClickListener(val clickListener: (song : Song) -> Unit){
    fun onClick(song : Song) = clickListener(song)
}