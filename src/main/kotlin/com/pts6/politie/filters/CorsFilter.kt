package com.pts6.politie.filters

import java.io.IOException
import javax.ws.rs.ForbiddenException
import javax.ws.rs.container.*
import javax.ws.rs.core.Response


@PreMatching
class CorsFilter : ContainerRequestFilter, ContainerResponseFilter {

    val allowedOrigins: MutableList<String> = mutableListOf()

    @Throws(IOException::class)
    override fun filter(requestContext: ContainerRequestContext) {
        val origin = requestContext.getHeaderString(HEADER_ORIGIN) ?: return
        if (requestContext.method.equals("OPTIONS", ignoreCase = true)) {
            preFlight(origin, requestContext)
        } else {
            checkOrigin(requestContext, origin)
        }
    }

    @Throws(IOException::class)
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {
        val origin = requestContext.getHeaderString(HEADER_ORIGIN)
        if (origin == null
                || requestContext.method.equals("OPTIONS", ignoreCase = true)
                || requestContext.getProperty("cors.failure") != null) {
            return
        }
        responseContext.headers.putSingle(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, origin)
        responseContext.headers.putSingle(HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
    }


    @Throws(IOException::class)
    private fun preFlight(origin: String, requestContext: ContainerRequestContext) {
        checkOrigin(requestContext, origin)
        val builder = Response.ok()
        builder.header(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN, origin)
        builder.header(HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")

        val requestMethods = requestContext.getHeaderString(HEADER_ACCESS_CONTROL_REQUEST_METHOD)
        if (requestMethods != null) {
            builder.header(HEADER_ACCESS_CONTROL_ALLOW_METHODS, requestMethods)
        }

        val allowHeaders = requestContext.getHeaderString(HEADER_ACCESS_CONTROL_REQUEST_HEADERS)
        if (allowHeaders != null) {
            builder.header(HEADER_ACCESS_CONTROL_ALLOW_HEADERS, allowHeaders)
        }
        requestContext.abortWith(builder.build())
    }

    private fun checkOrigin(requestContext: ContainerRequestContext, origin: String) {
        if (!allowedOrigins.contains("*") && !allowedOrigins.contains(origin)) {
            requestContext.setProperty("cors.failure", true)
            throw ForbiddenException("Origin not allowed: $origin")
        }
    }

    companion object {
        private const val HEADER_ORIGIN = "Origin"
        private const val HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin"
        private const val HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials"
        private const val HEADER_ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method"
        private const val HEADER_ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods"
        private const val HEADER_ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers"
        private const val HEADER_ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers"
    }
}