package com.pts6.politie.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.pts.europollib.EuropolVehicle
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class PolitieVehicle(
        val licensePlate: String = "",
        val originCountry: String = "",
        val serialNumber: String = ""
) : Serializable {

    constructor(europolVehicle: EuropolVehicle)
            : this(europolVehicle.licensePlate, europolVehicle.serialNumber, europolVehicle.originCountry)

    fun toEuropol() = EuropolVehicle(licensePlate, serialNumber, originCountry)
}