package com.pts6.politie.controller

import com.pts6.politie.domain.EuroStolenVehicle
import com.pts6.politie.service.EuropolStolenVehicleService
import com.pts6.politie.service.IStolenVehicleService
import com.pts62.common.finland.communication.rest.AntaminenService
import javax.ejb.EJB
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("vehicles/stolen")
class StolenController {

    @Inject
    private lateinit var europolStolenVehicleService: IStolenVehicleService

//    private val antaminenService = AntaminenService()

    @GET
    @Produces(APPLICATION_JSON)
    fun getVehicles():Response {
        return Response.ok(europolStolenVehicleService.getStolenVehicles()).build()
    }

    @Path("/{licensePlate}")
    @GET
    @Produces(APPLICATION_JSON)
    fun getVehicleByLicensePlate(@PathParam("licensePlate") licensePlate: String): Response {
        return Response.ok(europolStolenVehicleService.getVehicleByLicensePlate(licensePlate)).build()
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    fun insertStolenVehicle(stolenVehicle: EuroStolenVehicle): Response {
        return try {
            this.europolStolenVehicleService.insertStolenVehicle(stolenVehicle)
            Response.ok().build()
        } catch (e: Exception) {
            Response.serverError().entity(e).build()
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    fun deleteVehicle(@PathParam("id") id: String): Response {
        this.europolStolenVehicleService.deleteStolenVehicle(id)
        return Response.ok().build()
    }

    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    fun updateStolenVehicle(stolenVehicle: EuroStolenVehicle): Response {
        return try {
            this.europolStolenVehicleService.updateStolenVehicle(stolenVehicle)
            Response.ok().build()
        } catch (e: Exception) {
            Response.serverError().entity(e).build()
        }
    }
}