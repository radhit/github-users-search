package com.example.riskaadhita.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataResponse<T> {

    @SerializedName(value = "items")
    @Expose
    var items: T? = null

    @SerializedName(value = "total_count")
    @Expose
    var totalCount: Int = 0

    @SerializedName(value = "message")
    @Expose
    var message: String? = ""
}