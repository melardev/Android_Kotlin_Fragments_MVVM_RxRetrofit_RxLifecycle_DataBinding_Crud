package com.melardev.android.crud.datasource.common.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Todo : Parcelable {
    // For this annotations I needed: implementation 'com.squareup.retrofit2:converter-gson:2.6.0'

    @SerializedName("id")
    @Expose
    var id: Long? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("completed")
    @Expose
    var isCompleted: Boolean = false

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null


    constructor()

    protected constructor(`in`: Parcel) {
        if (`in`.readByte().toInt() == 0) {
            id = null
        } else {
            id = `in`.readLong()
        }
        title = `in`.readString()
        description = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        if (id == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeLong(id!!)
        }
        dest.writeString(title)
        dest.writeString(description)
    }

    companion object CREATOR : Parcelable.Creator<Todo> {
        override fun createFromParcel(parcel: Parcel): Todo {
            return Todo(parcel)
        }

        override fun newArray(size: Int): Array<Todo?> {
            return arrayOfNulls(size)
        }
    }
}
