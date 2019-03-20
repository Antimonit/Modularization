package me.khol.onboarding.room

import androidx.room.ColumnInfo

data class DBAddress(
    @ColumnInfo val street: String,
    @ColumnInfo val city: String,
    @ColumnInfo val zipCode: String,
    @ColumnInfo val streetNo: String
) {

    override fun toString(): String {
        return "$street $streetNo, $zipCode, $city"
    }
}
