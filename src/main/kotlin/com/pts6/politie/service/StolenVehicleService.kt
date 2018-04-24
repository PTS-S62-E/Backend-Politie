package com.pts6.politie.service

import com.pts6.politie.domain.StolenVehicle
import io.github.cdimascio.dotenv.Dotenv
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Stateless
class StolenVehicleService {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun getAllStolenVehicles(isStolen: Boolean = true): List<StolenVehicle> =
            this.entityManager
                    .createNamedQuery("stolenvehicle.ifstolen")
                    .setParameter("is_stolen", isStolen)
                    .resultList
                    .filterIsInstance<StolenVehicle>()

    fun insertStolenVehicle(stolenVehicle: StolenVehicle) {
        this.entityManager.persist(stolenVehicle)
    }

    fun updateStolenVehicle(stolenVehicle: StolenVehicle) {
        this.entityManager.merge(stolenVehicle)
    }
}