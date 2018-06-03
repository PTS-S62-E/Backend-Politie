package com.pts6.politie.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.pts.europollib.EuropolVehicle
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class PolitieVehicle(
        val HashedLicensePlate: String = "",
        val originCountry: String = "",
        val serialNumber: String = ""
) : Serializable {

    constructor(europolVehicle: EuropolVehicle)
            : this(europolVehicle.HashedLicensePlate, europolVehicle.serialNumber, europolVehicle.originCountry)

    fun toEuropol() = EuropolVehicle(HashedLicensePlate, serialNumber, originCountry)
}