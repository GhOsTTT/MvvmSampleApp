package com.pasaoglu.movieapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pasaoglu.movieapp.data.model.MovieListItemResponseModel
import com.pasaoglu.movieapp.databinding.MovieItemLayoutBinding


class MovieListAdapter(
    private val movieItemResponseList: ArrayList<MovieListItemResponseModel>,
    private val listener: (movieItemResponse: MovieListItemResponseModel, imageView: ImageView) -> Unit
) :
    ListAdapter<MovieListItemResponseModel, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    private class DataViewHolder(
        private val binding: MovieItemLayoutBinding,
        var listener: (movieItemResponse: MovieListItemResponseModel, imageView: ImageView) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                listener.invoke(binding.movieItemModel!!, binding.posterImageView)
            }
        }

        fun bind(itemResponse: MovieListItemResponseModel) {
            binding.apply {
                movieItemModel = itemResponse
                executePendingBindings()
            }
        }
    }


    override fun getItemCount(): Int = movieItemResponseList.size


    fun addMovies(movieItemResponseList: List<MovieListItemResponseModel>) {
        this.movieItemResponseList.apply {
            clear()
            addAll(movieItemResponseList)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = MovieItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding, listener)
    }




    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemViewHolder = holder as DataViewHolder
        itemViewHolder.bind(movieItemResponseList[position])
    }
}

private class MovieDiffCallback : DiffUtil.ItemCallback<MovieListItemResponseModel>() {
    override fun areItemsTheSame(oldItem: MovieListItemResponseModel, newItem: MovieListItemResponseModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieListItemResponseModel, newItemResponse: MovieListItemResponseModel): Boolean {
        return oldItem == newItemResponse
    }
}