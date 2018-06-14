package com.pts6.politie.controller

import com.pts62.common.finland.communication.rest.AntaminenService
import com.pts62.common.finland.communication.rest.IServiceConfiguration
import com.pts62.common.finland.communication.rest.api.GetVehicleByLicensePlateResponse
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("vehicles/ownership")
class VehicleOwnershipController {

    private val antaminenService = AntaminenService()

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

}