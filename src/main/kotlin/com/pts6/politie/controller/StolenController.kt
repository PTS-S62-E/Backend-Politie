package com.pts6.politie.controller

import com.pts.europollib.EuropolLib
import com.pts.europollib.EuropolVehicle
import com.pts6.politie.domain.PolitieVehicle
import com.pts62.common.finland.communication.RegistrationMovementService
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("vehicles/stolen")
class StolenController {

    private val europolLib = EuropolLib()

    @GET
    @Produces(APPLICATION_JSON)
    fun getStolenVehicles(@QueryParam("search") search: String?): Response {
        val vehicles: List<EuropolVehicle> = if (search == null) {
            europolLib.getVehicles()
        } else {
            europolLib.getVehicleWithSearch(search)
        }
        return Response.ok(vehicles.filter { it.originCountry == "FI" }).build()
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
        val vehicle = RegistrationMovementService.getInstance().getVehicleByLicensePlate(stolenVehicle.licensePlate)
        val serialNumber = vehicle!!.serialNumber
        val completeStolenVehicle = stolenVehicle.copy(serialNumber = serialNumber)

        return try {
            val v = this.europolLib.insertVehicle(completeStolenVehicle.toEuropol())
            Response.ok(v).build()
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