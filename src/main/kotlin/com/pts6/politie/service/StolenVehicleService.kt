package com.pts6.politie.service

import com.pts6.politie.domain.StolenVehicle
import org.hibernate.search.exception.EmptyQueryException
import org.hibernate.search.jpa.FullTextEntityManager
import org.hibernate.search.jpa.Search
import javax.ejb.Stateless
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.PersistenceContext
import javax.persistence.PersistenceUnit

@Stateless
class StolenVehicleService {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @PersistenceUnit
    private lateinit var entityManagerFactory: EntityManagerFactory

    private var fullTextEntityManager: FullTextEntityManager? = null

    fun getAllStolenVehicles(isStolen: Boolean = true): List<StolenVehicle> =
            this.entityManager
                    .createNamedQuery("stolenvehicle.ifstolen")
                    .setParameter("is_stolen", isStolen)
                    .resultList
                    .filterIsInstance<StolenVehicle>()

    fun insertStolenVehicle(stolenVehicle: StolenVehicle) {
        this.entityManager.persist(stolenVehicle)
    }

    fun getStolenVehicleById(id: Long) = this.entityManager.find(StolenVehicle::class.java, id)

    fun updateStolenVehicle(stolenVehicle: StolenVehicle) {
        this.entityManager.merge(stolenVehicle)
    }

    fun deleteStolenVehicle(id: Long) {
        this.entityManager.remove(this.entityManager.find(StolenVehicle::class.java, id))
    }

    private fun getFullTextEntityManager(): FullTextEntityManager {
        if (this.fullTextEntityManager != null) {
            return this.fullTextEntityManager!!
        }
        val searchEntityManager = this.entityManagerFactory.createEntityManager()
        this.fullTextEntityManager = Search.getFullTextEntityManager(searchEntityManager)
        this.fullTextEntityManager?.createIndexer()?.startAndWait()
        return this.fullTextEntityManager!!
    }

    fun searchStolenVehicles(query: String): List<StolenVehicle> {
        val profileQueryBuilder = this.getFullTextEntityManager().searchFactory.buildQueryBuilder().forEntity(StolenVehicle::class.java).get()
        return try {
            val profileQuery = profileQueryBuilder
                    .keyword()
                    .onFields("licensePlate", "hardwareSn", "stolen")
                    .matching(query)
                    .createQuery()
            this.getFullTextEntityManager()
                    .createFullTextQuery(profileQuery)
                    .resultList
                    .filterIsInstance<StolenVehicle>()
        } catch (e: EmptyQueryException) {
            emptyList()
        }
    }
}