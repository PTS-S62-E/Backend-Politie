package com.pts6.politie.controller

import com.pts6.politie.service.TrackingService
import io.sentry.Sentry
import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("vehicles/track")
class TrackingController{

    @Inject
    private lateinit var trackingService: TrackingService

    @POST
    @Path("/")
    @Consumes(MediaType.TEXT_PLAIN)
    fun create(licensePlate: String): Response {
        try {
            trackingService.trackCar(licensePlate)
            return Response.ok().build()
        } catch (e: Exception) {
            Sentry.capture(e.toString())
            throw WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build())
        }

    }

    @DELETE
    @Path("/{licensePlate}")
    @Consumes(MediaType.TEXT_PLAIN)
    fun delete(@PathParam("licensePlate") licensePlate: String): Response {
        try {
            trackingService.stopTrackingCar(licensePlate)
            return Response.ok().build()
        } catch (e: Exception) {
            Sentry.capture(e.toString())
            throw WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.message).build())
        }
    }
}