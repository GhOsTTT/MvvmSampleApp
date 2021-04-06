package com.pasaoglu.movieapp.data.model

import com.google.gson.annotations.SerializedName
import com.pasaoglu.movieapp.BuildConfig


data class MovieListItemResponseModel(
    @SerializedName("poster_path")
    private var poster_path: String? = null,
    @SerializedName("adult")
    var adult: Boolean? = null,
    @SerializedName("overview")
    var overview: String? = null,
    @SerializedName("release_date")
    var releaseDate: String? = null,
    @SerializedName("genre_ids")
    var genreIds: List<Int>? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("original_title")
    var originalTitle: String? = null,
    @SerializedName("original_language")
    var originalLanguage: String? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("backdrop_path")
    private var backdropPath: String? = null,
    @SerializedName("vote_count")
    var voteCount: Int? = null,
    @SerializedName("vote_average")
    var voteAverage: Float? = null,

){
    fun getPosterImagePath():String?{
        return if (poster_path.isNullOrBlank()) null else "${BuildConfig.IMAGE_URL}$poster_path"
    }

    fun getBackDropImagePath():String?{
        return if (backdropPath.isNullOrBlank()) null else "${BuildConfig.IMAGE_URL}$backdropPath"
    }
}