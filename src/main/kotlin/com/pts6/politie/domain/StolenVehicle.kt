package com.pts6.politie.domain

import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import java.io.Serializable
import javax.persistence.*

@Entity
@NamedQueries(
        NamedQuery(name = "stolenvehicle.ifstolen", query = "SELECT sv FROM StolenVehicle sv WHERE sv.stolen = :is_stolen")
)
@Indexed
data class StolenVehicle(
        @Id
        @GeneratedValue
        val id: Long = -1L,
        @Column
        @Field
        val licensePlate: String = "",
        @Column
        @Field
        val hardwareSn: String = "",
        @Column
        @Field
        val stolen: Boolean = false
) : Serializable