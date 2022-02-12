package app.com.crawlmp3.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.com.crawlmp3.databinding.AlbumElementViewBinding

import app.com.crawlmp3.network.Album

class HomeAdapter(
    private val clickListener: OnClickListener) : ListAdapter<Album, HomeAdapter.ViewHolder>(DiffCallBack){
    companion object DiffCallBack : DiffUtil.ItemCallback<Album>() {
        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem===newItem
        }

    }

    class ViewHolder private constructor(
        val binding : AlbumElementViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album?, clickListener: OnClickListener) {
            binding.album = album
            binding.onListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AlbumElementViewBinding
                                .inflate(layoutInflater,parent,false)

                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = getItem(position)
        holder.bind(album,clickListener)
    }
}

class OnClickListener(val clickListener: (idAlbum : String) -> Unit){
    fun onClick(album : Album) = clickListener(album.id)
}
