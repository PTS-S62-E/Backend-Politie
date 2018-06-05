package com.pts6.politie.service

import com.pts62.common.finland.communication.RegistrationMovementService
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Stateless
class TrackingService{

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    /**
     * Contacts the registration movement to add a tracking to the database.
     */
    fun trackCar(licensePlate: String){
        RegistrationMovementService.getInstance().setBaseUrl("http://127.0.0.1:8080/registratie-verplaatsing")
        RegistrationMovementService.getInstance().createTracking(licensePlate)
    }

    /**
     * Contacts the registration movement to remove a tracking from the database.
     */
    fun stopTrackingCar(licensePlate: String){
        RegistrationMovementService.getInstance().setBaseUrl("http://127.0.0.1:8080/registratie-verplaatsing")
        RegistrationMovementService.getInstance().deleteTracking(licensePlate)
    }

}