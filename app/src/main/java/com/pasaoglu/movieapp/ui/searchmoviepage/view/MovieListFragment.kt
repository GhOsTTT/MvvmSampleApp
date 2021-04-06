package com.pasaoglu.movieapp.ui.searchmoviepage.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pasaoglu.movieapp.R
import com.pasaoglu.movieapp.data.model.MovieListItemResponseModel
import com.pasaoglu.movieapp.databinding.MovieListFragmentBinding
import com.pasaoglu.movieapp.ui.adapter.MovieListAdapter
import com.pasaoglu.movieapp.ui.searchmoviepage.viewmodel.MovieListViewModel
import com.pasaoglu.movieapp.util.Status
import com.pasaoglu.movieapp.util.isNetworkAvailable
import com.pasaoglu.movieapp.util.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieListFragment : Fragment() {

    companion object {
        fun newInstance() = MovieListFragment()
    }

    private var _binding: MovieListFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            3
        )
        movieListAdapter = MovieListAdapter(arrayListOf()) { movieItem, imageView->
            val action = MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(
                movieId = movieItem.id!!,
                movieTitle = movieItem.title!!,
                voteAverage = movieItem.voteAverage!!,
                posterPath = movieItem.getPosterImagePath(),
                backdropPath = movieItem.getBackDropImagePath()
            )
            if(movieItem.getPosterImagePath() != null){
                val extras = FragmentNavigatorExtras(
                    imageView to movieItem.getPosterImagePath()!!
                )
                view?.findNavController()?.navigate(action, extras)
            }else{
                view?.findNavController()?.navigate(action)
            }
        }

        binding.recyclerView.apply {
            adapter = movieListAdapter
              postponeEnterTransition()
            viewTreeObserver
                .addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                        viewModel.getSearchedMovieListWithQuery(query = binding.queryEditText.text.toString(), isEditing = false)

                    }
                }
            })
        }
        binding.queryEditText.doOnTextChanged { inputText, _, _, _ ->
            viewModel.getSearchedMovieListWithQuery(query = inputText.toString(), isEditing = true)
        }
    }

    private fun setupObservers() {
        viewModel.searchResult.observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.recyclerView.visible(true)
                        binding.progressBar.visible(false)
                        resource.data?.let {

                        }
                        resource.data?.let { movieList -> retrieveList(movieList) }
                    }
                    Status.ERROR -> {
                        if (!requireActivity().isNetworkAvailable()) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.no_internet_connection_offline_mode_cannot_work),
                                Toast.LENGTH_LONG
                            ).show()
                        } 
                        binding.recyclerView.visible(true)
                        binding.progressBar.visible(false)
                    }
                    Status.LOADING -> {
                        binding.progressBar.visible(true)
                        // binding.recyclerView.visible(false)
                    }
                    Status.EMPTY -> {
                        binding.progressBar.visible(false)
                        retrieveList(mutableListOf())
                    }
                }
            }
        })
    }

    private fun retrieveList(movieListItemResponse: MutableList<MovieListItemResponseModel>) {
        movieListAdapter.apply {
            addMovies(movieListItemResponse)
            notifyDataSetChanged()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}