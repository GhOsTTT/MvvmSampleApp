package com.pasaoglu.movieapp.data.model

import com.google.gson.annotations.SerializedName

data class MovieDetailResponseModel(
    @SerializedName("backdrop_path")
    var backdropPath: String? = null,
    @SerializedName("overview")
    var overview: String? = null,
    @SerializedName("vote_average")
    var voteAverage: String? = null,
    @SerializedName("genres")
    var genres: List<GenresResponseModel>? = null
)