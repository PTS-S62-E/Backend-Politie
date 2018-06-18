package com.pts6.politie.controller

import com.pts62.common.finland.communication.rest.AntaminenService
import com.pts62.common.finland.communication.rest.IServiceConfiguration
import com.pts62.common.finland.communication.rest.api.GetVehicleByLicensePlateResponse
import java.util.*
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("vehicles/ownership")
class VehicleOwnershipController {

    private val antaminenService = AntaminenService()
    private val base64Decoder = Base64.getDecoder()

    init {
        this.antaminenService.overrideConfig(object : IServiceConfiguration {
            override fun getServiceAddress() = antaminenService.getDefaultConfig().getServiceAddress()
            override fun getServiceCredential() = "juleskreutzer@me.com"
            override fun getServicePassword() = "kreutzer"
        })
    }

    @GET
    @Path("/{licensePlate}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getOwnership(@PathParam("licensePlate") licensePlate: String): GetVehicleByLicensePlateResponse {
        return this.antaminenService.getVehicleByLicensePlate(licensePlate)
    }

    @GET
    @Path("/")
    @Produces
    fun getOwnershipBodyBase64(@QueryParam("licensePlateBase64") licensePlateB64: String): GetVehicleByLicensePlateResponse {
        val licensePlate = this.base64Decoder.decode(licensePlateB64).toString()
        return this.getOwnership(licensePlate)
    }

}