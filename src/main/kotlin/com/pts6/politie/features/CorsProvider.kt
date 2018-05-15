package com.pts6.politie.features

import com.pts6.politie.filters.CorsFilter
import javax.ws.rs.core.Feature
import javax.ws.rs.core.FeatureContext
import javax.ws.rs.ext.Provider

@Provider
open class CorsProvider : Feature {

    @Override
    override fun configure(context: FeatureContext): Boolean {
        val corsFilter = CorsFilter()
        corsFilter.allowedOrigins.add("*")
        context.register(corsFilter)
        return true
    }
}