package com.pasaoglu.movieapp.data.model

import com.google.gson.annotations.SerializedName


data class BaseMovieListResponseModel<T>(
        @SerializedName("page")
        var page: Int? = null,
        @SerializedName("total_pages")
        var totalPage: Int? = null,
        @SerializedName("total_results")
        var totalResults: Int? = null,
        @SerializedName("results")
        var results: MutableList<T>? = null
)