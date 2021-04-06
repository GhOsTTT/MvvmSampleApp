package com.pasaoglu.movieapp.ui.moviedetailpage.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.pasaoglu.movieapp.R
import com.pasaoglu.movieapp.databinding.MovieDetailFragmentBinding
import com.pasaoglu.movieapp.ui.MainActivity
import com.pasaoglu.movieapp.ui.moviedetailpage.viewmodel.MovieDetailViewModel
import com.pasaoglu.movieapp.util.Status
import com.pasaoglu.movieapp.util.isNetworkAvailable
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    companion object {
        fun newInstance() = MovieDetailFragment()
    }
    private var _binding: MovieDetailFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        setupObservers()
        val posterPath = args.posterPath
        binding.moviePosterImageView.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                transitionName = posterPath
            }
            Glide.with(this)
                .load(posterPath)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(this)
        }

        binding.movieBackdropImageView.apply {
            Glide.with(this)
                .load(args.backdropPath)
                .transform(CenterCrop())
                .into(this)
        }

        postponeEnterTransition()
        requireView().doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).actionBar?.title = args.movieTitle
        (requireActivity() as MainActivity).title = args.movieTitle
    }

    private fun setupObservers() {
        viewModel.getMovieDetailWithMovieId(movieId = args.movieId).observe(requireActivity(), {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.movieOverviewTextView.text =  resource.data?.overview ?: ""
                        val tempGenresText = StringBuilder()
                        resource.data?.genres?.forEach {
                            genresModel ->
                            tempGenresText.appendLine( genresModel.name)
                        }
                        binding.movieGenresTextView.text = tempGenresText
                        binding.movieVoteAverageTextView.text = resource.data?.voteAverage ?: ""
                    }
                    Status.ERROR -> {
                        if (!requireActivity().isNetworkAvailable()) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.no_internet_connection_offline_mode_cannot_work),
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}