package com.vcmanea.flickerapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
//Kotlin privedes its own Serializable mechanism
// Serializable can be faster than parcelable in some cases, especially when fully implemented, exmaple phot class
@Parcelize
data class PhotoParcelable(var title: String, var author: String, var authorId: String, var tags: String, var imageUrlSmall: String, var imgUrlBig: String) :Parcelable {
}