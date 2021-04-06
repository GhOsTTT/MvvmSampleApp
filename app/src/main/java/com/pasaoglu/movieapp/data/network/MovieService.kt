package com.pasaoglu.movieapp.data.network


import com.pasaoglu.movieapp.data.model.BaseMovieListResponseModel
import com.pasaoglu.movieapp.data.model.MovieDetailResponseModel
import com.pasaoglu.movieapp.data.model.MovieListItemResponseModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieService {

    @GET("search/movie")
    suspend fun getMovieListWithQuery(@Query("query")query:String, @Query("page")page:Int): BaseMovieListResponseModel<MovieListItemResponseModel>

    @GET("movie/{movieId}")
    suspend fun getMovieDetailWithMovieId(@Path("movieId")movieId:Int): MovieDetailResponseModel

}