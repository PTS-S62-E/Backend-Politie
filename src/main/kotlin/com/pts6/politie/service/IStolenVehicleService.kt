package com.pts6.politie.service

import com.pts6.politie.domain.EuroStolenVehicle
import javax.ejb.Stateless

@Stateless
interface IStolenVehicleService {
    fun insertStolenVehicle(euroStolenVehicle: EuroStolenVehicle): Boolean

    fun getVehicleByLicensePlate(licensePlate: String): EuroStolenVehicle

    fun updateStolenVehicle(euroStolenVehicle: EuroStolenVehicle): Boolean

    fun deleteStolenVehicle(id: String): Boolean

    fun getStolenVehicles(): List<EuroStolenVehicle>
}