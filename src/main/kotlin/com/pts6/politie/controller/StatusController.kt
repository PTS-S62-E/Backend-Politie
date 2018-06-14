package com.pts6.politie.controller

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.core.Response

@Path("/status")
class StatusController {

    @GET
    @Path("/")
    fun getStatus(): Response {
        return Response.ok().build();
    }

}