package com.pts6.politie.controller

import com.pts.europollib.EuropolLib
import com.pts.europollib.EuropolVehicle
import com.pts6.politie.domain.PolitieVehicle
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("vehicles/stolen")
class StolenController {

    private val europolLib = EuropolLib()

    @GET
    @Produces(APPLICATION_JSON)
    fun getStolenVehicles(): Response {
        return Response.ok(europolLib.getVehicles()).build()
    }

    @GET
    @Path("/{serialNumber}")
    @Produces(APPLICATION_JSON)
    fun getStolenVehicleById(@PathParam("serialNumber") serialNumber: String): Response {
        return Response.ok(this.europolLib.getVehicleBySerial(serialNumber)).build()
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    fun insertStolenVehicle(stolenVehicle: PolitieVehicle): Response {
        return try {
            this.europolLib.insertVehicle(stolenVehicle.toEuropol())
            Response.ok().build()
        } catch (e: Exception) {
            Response.serverError().entity(e).build()
        }
    }

    @DELETE
    @Produces(APPLICATION_JSON)
    @Path("/{serialNumber}")
    fun deleteVehicle(@PathParam("serialNumber") serialNumber: String): Response {
        this.europolLib.deleteVehicle(serialNumber)
        return Response.ok().build()
    }
}