package com.fernandopretell.rappidemo.model

import android.os.Parcel
import android.os.Parcelable

class CardModelParcelable(
    val id_remote: Int,
    val original_title: String?,
    val vote_count: Int,
    val popularity: Double,
    val poster_path: String?,
    val backdrop_path: String?,
    val video: Boolean,
    val adult: Boolean,
    val vote_average: Double,
    val overview: String?,
    val release_date: String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id_remote)
        parcel.writeString(original_title)
        parcel.writeInt(vote_count)
        parcel.writeDouble(popularity)
        parcel.writeString(poster_path)
        parcel.writeString(backdrop_path)
        parcel.writeByte(if (video) 1 else 0)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeDouble(vote_average)
        parcel.writeString(overview)
        parcel.writeString(release_date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CardModelParcelable> {
        override fun createFromParcel(parcel: Parcel): CardModelParcelable {
            return CardModelParcelable(parcel)
        }

        override fun newArray(size: Int): Array<CardModelParcelable?> {
            return arrayOfNulls(size)
        }
    }
}