package com.pts6.politie.domain

import javax.persistence.*

@Entity
@NamedQueries(
        NamedQuery(name = "stolenvehicle.ifstolen", query = "SELECT sv FROM StolenVehicle sv WHERE sv.stolen = :is_stolen")
)
data class StolenVehicle(
        @Id
        val licensePlate: String,
        @Column
        val stolen: Boolean = false
) {

    fun getOwners() {

    }
}