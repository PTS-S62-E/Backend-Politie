package com.pts6.politie.controller

import com.pts6.politie.domain.StolenVehicle
import com.pts6.politie.service.StolenVehicleService
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("vehicles/stolen")
class StolenController {

    @Inject
    private lateinit var stolenVehicleService: StolenVehicleService

    @GET
    @Produces(APPLICATION_JSON)
    fun getStolenVehicles(): Response {
        return Response.ok(stolenVehicleService.getAllStolenVehicles()).build()
    }

    @GET
    @Path("/{vehicleId}")
    @Produces(APPLICATION_JSON)
    fun getStolenVehicleById(id: Long): Response {
        return Response.ok(this.stolenVehicleService.getStolenVehicleById(id)).build()
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    fun insertStolenVehicle(stolenVehicle: StolenVehicle): Response {
        return try {
            this.stolenVehicleService.insertStolenVehicle(stolenVehicle)
            Response.ok().build()
        } catch (e: Exception) {
            Response.serverError().entity(e).build()
        }
    }

    @DELETE
    @Produces(APPLICATION_JSON)
    fun deleteVehicle(@PathParam("id") id: Long): Response {
        this.stolenVehicleService.deleteStolenVehicle(id)
        return Response.ok().build()
    }

    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    fun updateStolenVehicle(stolenVehicle: StolenVehicle): Response {
        return try {
            this.stolenVehicleService.updateStolenVehicle(stolenVehicle)
            Response.ok().build()
        } catch (e: Exception) {
            Response.serverError().entity(e).build()
        }
    }

    @GET
    @Path("/search/{query}")
    @Produces(APPLICATION_JSON)
    fun getStolenVehicleSearch(@PathParam("query") query: String): Response {
        val vehicles = this.stolenVehicleService.searchStolenVehicles(query)
        return Response.ok(vehicles).build()
    }

}