package `in`.covid360rf.covid360

import android.os.Parcel
import android.os.Parcelable

data class CovidInfo (
    var state: String,
    var active: String,
    var deaths: String,
    var confirmed: String,
    var recovered: String

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
       return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<CovidInfo> {
        override fun createFromParcel(parcel: Parcel): CovidInfo {
            return CovidInfo(parcel)
        }

        override fun newArray(size: Int): Array<CovidInfo?> {
            return arrayOfNulls(size)
        }
    }
}