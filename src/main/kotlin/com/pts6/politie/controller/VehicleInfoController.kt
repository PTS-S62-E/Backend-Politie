package com.pts6.politie.controller

import com.pts62.common.finland.communication.RegistrationMovementService
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("vehicles/info")
class VehicleInfoController {

    @GET
    @Path("/{licensePlate}")
    @Produces(APPLICATION_JSON)
    fun getVehicleInfo(@PathParam("licensePlate") licensePlate: String): Response {
        val registrationMovementService = RegistrationMovementService.getInstance()
        val vehicle = registrationMovementService.getVehicleByLicensePlate(licensePlate)
        return Response.ok(vehicle).build()
    }

    @GET
    @Path("/{licensePlate}/{startDate}/{endDate}")
    @Produces(APPLICATION_JSON)
    fun getVehicleMovementHistory(@PathParam("licensePlate") licensePlate: String,
                                  @PathParam("startDate") startDate: String,
                                  @PathParam("endDate") endDate: String): Response {
        println("called")
        val registrationMovementService = RegistrationMovementService.getInstance()
        val administrationDto = registrationMovementService.getTranslocationsByLicensePlate(licensePlate, startDate, endDate)
        return Response.ok(administrationDto).build()
    }

}