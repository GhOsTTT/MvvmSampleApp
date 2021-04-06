package com.pasaoglu.movieapp.data.model

import com.google.gson.annotations.SerializedName

data class GenresResponseModel(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null
)
