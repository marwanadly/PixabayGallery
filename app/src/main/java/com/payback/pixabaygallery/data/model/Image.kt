package com.payback.pixabaygallery.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.payback.pixabaygallery.util.capitalizeWords
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "images")
@Parcelize
data class Image(

    @PrimaryKey(autoGenerate = true)
    var image_id: Long,

    @SerializedName("id")
    var id: Long,

    @SerializedName("webformatURL")
    var imageURL: String,

    @SerializedName("largeImageURL")
    var largeImageURL: String,

    @SerializedName("tags")
    var tags: String,

    @SerializedName("user")
    var username: String,

    @SerializedName("userImageURL")
    var userImageURL: String,

    @SerializedName("favorites")
    var favorites: Int,

    @SerializedName("comments")
    var comments: Int,

    @SerializedName("likes")
    var likes: Int,

    var searchQuery: String,

    var pageNumber: Int
) : Parcelable {
    fun getNormalizedTagsNames(): String =
        tags.replace(',', ' ').capitalizeWords()

}