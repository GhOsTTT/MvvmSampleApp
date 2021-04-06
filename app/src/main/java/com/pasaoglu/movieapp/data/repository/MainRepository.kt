package com.pasaoglu.movieapp.data.repository

import com.pasaoglu.movieapp.data.network.MovieService
import javax.inject.Inject

class  MainRepository @Inject constructor(private val movieService: MovieService){

    suspend fun getMovieListWithQuery(query: String, page: Int) = movieService.getMovieListWithQuery(query = query, page = page)

    suspend fun getMovieDetailWithMovieId(movieId: Int) = movieService.getMovieDetailWithMovieId(movieId = movieId)

}