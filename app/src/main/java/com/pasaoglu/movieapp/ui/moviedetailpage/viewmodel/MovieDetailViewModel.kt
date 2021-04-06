package com.pasaoglu.movieapp.ui.moviedetailpage.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.pasaoglu.movieapp.data.repository.MainRepository
import com.pasaoglu.movieapp.util.Resource
import kotlinx.coroutines.Dispatchers


class MovieDetailViewModel @ViewModelInject constructor(private val mainRepository:MainRepository ) : ViewModel() , LifecycleObserver {

    fun getMovieDetailWithMovieId(movieId: Int) = liveData(Dispatchers.IO) {
            try {
                emit(Resource.success(data = mainRepository.getMovieDetailWithMovieId(movieId = movieId)))
            } catch (exception: Exception) {
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
}