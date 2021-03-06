package com.pts6.politie.service

import com.pts62.common.finland.communication.RegistrationMovementService
import javax.ejb.Stateless

@Stateless
class TrackingService{

    /**
     * Contacts the registration movement to add a tracking to the database.
     */
    fun trackCar(licensePlate: String){
        RegistrationMovementService.getInstance().createTracking(licensePlate)
    }

    /**
     * Contacts the registration movement to remove a tracking from the database.
     */
    fun stopTrackingCar(licensePlate: String){
        RegistrationMovementService.getInstance().deleteTracking(licensePlate)
    }

}