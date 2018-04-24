package com.pts6.politie.controller

import com.pts6.politie.service.StolenVehicleService
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("vehicles/stolen")
class StolenController {

    @Inject
    private lateinit var stolenVehicleService: StolenVehicleService

    @GET
    fun getStolenVehicles(): Response {
        return Response.ok(stolenVehicleService.getAllStolenVehicles()).build()
    }

}