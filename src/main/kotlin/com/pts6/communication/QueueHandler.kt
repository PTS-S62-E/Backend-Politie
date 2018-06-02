package com.pts6.communication

import com.fasterxml.jackson.databind.ObjectMapper
import com.pts6.politie.websockets.TrackingSocket
import com.pts62.common.finland.communication.CommunicationBuilder
import com.pts62.common.finland.communication.IQueueSubscribeCallback
import com.pts62.common.finland.communication.QueueConnector
import com.pts62.common.finland.dto.TrackingInfoDto
import io.sentry.Sentry
import io.sentry.event.BreadcrumbBuilder
import javax.annotation.PostConstruct
import javax.ejb.Startup
import javax.inject.Singleton

@Startup
@Singleton
class QueueHandler{

companion object {

    val country = "fi"
    val application = "police"
}

    private var connector: QueueConnector? = null

    @PostConstruct
    private fun setup() {
        println("SETTING UP QUEUE*************************************************************************************************")
        connector = QueueConnector()
        this.handleRead()
    }

    private fun handleRead() {
        //TODO: set actual values
        val policeBuilder = CommunicationBuilder()
        policeBuilder.setCountry(country)
        policeBuilder.setApplication(application)
        policeBuilder.setMessage("track.vehicle")

        val comBuilder = CommunicationBuilder()
        comBuilder.setCountry(country)
        comBuilder.setApplication(application)
        comBuilder.setMessage("*")

        connector!!.readMessage(policeBuilder.build(), object : IQueueSubscribeCallback {
            override fun onMessageReceived(message: String) {
                try {
                    println("POLICE SYSTEM RECIEVED MESSAGE FROM REGISTRATION")
                    val mapper = ObjectMapper()
                    val trackingInfoDto = mapper.readValue(message, TrackingInfoDto::class.java)
                    TrackingSocket.sendMessage(trackingInfoDto)
                } catch (e: Exception) {
                    Sentry.getContext().recordBreadcrumb(BreadcrumbBuilder().setMessage("Error in recieving tracking").build())
                    Sentry.capture(e)
                }

            }
        })

        //This method handles messages that get send to
        connector!!.readMessage(comBuilder.build(), object : IQueueSubscribeCallback {
            override fun onMessageReceived(s: String) {
                val e = Exception("Unable to process MQ request for specific route with data $s")
                Sentry.getContext().recordBreadcrumb(BreadcrumbBuilder().setMessage(s).build())
                Sentry.capture(e)
            }
        })
    }
}