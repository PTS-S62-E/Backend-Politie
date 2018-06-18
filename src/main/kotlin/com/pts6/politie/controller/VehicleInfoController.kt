package com.pts6.politie.controller

import com.pts62.common.finland.communication.RegistrationMovementService
import io.sentry.Sentry
import javax.ws.rs.*
import java.util.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import javax.ws.rs.core.Response

@Path("vehicles/info")
class VehicleInfoController {

    private val base64Decoder = Base64.getDecoder()

    @GET
    @Path("/{licensePlate}")
    @Produces(APPLICATION_JSON)
    fun getVehicleInfo(@PathParam("licensePlate") licensePlate: String): Response {
        try {
            val registrationMovementService = RegistrationMovementService.getInstance()
            val vehicle = registrationMovementService.getVehicleByLicensePlate(licensePlate)
            return Response.ok(vehicle).build()
        }
        catch (e: Exception) {
            Sentry.capture(e.toString())
            throw WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build())
        }
    }

    @GET
    @Path("/{licensePlate}/{startDate}/{endDate}")
    @Produces(APPLICATION_JSON)
    fun getVehicleMovementHistory(@PathParam("licensePlate") licensePlate: String,
                                  @PathParam("startDate") startDate: String,
                                  @PathParam("endDate") endDate: String): Response {
        try {
            val registrationMovementService = RegistrationMovementService.getInstance()
            val administrationDto = registrationMovementService.getTranslocationsByLicensePlate(licensePlate, startDate, endDate)
            return Response.ok(administrationDto).build()
        }
        catch (e: Exception) {
            Sentry.capture(e.toString())
            throw WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build())
        }
    }

    @GET
    @Path("/")
    @Produces
    fun getVehicleInfoBodyBase64(@QueryParam("licensePlateBase64") licensePlateB64: String): Response {
        val licensePlate = this.base64Decoder.decode(licensePlateB64).toString()
        return this.getVehicleInfo(licensePlate)
    }

}