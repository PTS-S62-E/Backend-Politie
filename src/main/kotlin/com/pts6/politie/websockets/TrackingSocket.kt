package com.pts6.politie.websockets

import com.pts62.common.finland.communication.RegistrationMovementService
import com.pts62.common.finland.dto.TrackingInfoDto
import com.pts62.common.toJson
import javax.websocket.OnClose
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.server.PathParam
import javax.websocket.server.ServerEndpoint
import javax.websocket.CloseReason



@ServerEndpoint("/tracking")
class TrackingSocket {

    @OnOpen
    fun onOpen(session: Session, @PathParam("licensePlate") l: String?) {
        println("User connected")
        val licensePlate = "LICENSPLATE"
        sessionLicensePlates.put(session, licensePlate)
        RegistrationMovementService.getInstance().setBaseUrl("http://127.0.0.1:8080/registratie-verplaatsing")

        try {
            RegistrationMovementService.getInstance().getVehicleByLicensePlate(licensePlate)
            println("---adding tracking to registrationdb---")
            RegistrationMovementService.getInstance()
            RegistrationMovementService.getInstance().createTracking(licensePlate)
        }
        catch(e: Exception){
            sessionLicensePlates.remove(session)
            session.close(CloseReason(CloseReason.CloseCodes.UNEXPECTED_CONDITION, e.toString()))
            e.printStackTrace()
        }

        println("${Companion.sessionLicensePlates.size} sessions active")
    }

    @OnClose
    fun onClose(session: Session) {
        println("---ONCLOSE---")
        val licensePlate = sessionLicensePlates.get(session)
        if (licensePlate != null) {
            //Check if there's more than one session tracking the licensePlate
            val numberOfSessions = getSessionFollowingLicensePlate(licensePlate)

            if (numberOfSessions == 1){
                RegistrationMovementService.getInstance().deleteTracking(licensePlate)
            }
            println("---REMOVING TRACKING---")
            sessionLicensePlates.remove(session)
        }
    }

    fun getSessionFollowingLicensePlate(licensePlate: String): Int {
        var count = 0
        for (pair in sessionLicensePlates) {
            if (pair.value.equals(licensePlate)) count++
        }
        println(count)
        return count
    }

    companion object {
        val sessionLicensePlates = mutableMapOf<Session, String>()

        fun sendMessage(trackingInfoDto: TrackingInfoDto){
            println("sending message")
            println(trackingInfoDto.licensePlate)
            println(trackingInfoDto.translocationDto.serialNumber)

            sessionLicensePlates.forEach {
                println("looping")
                if(it.value.equals(trackingInfoDto.licensePlate)) {
                    println("found")
                    it.key.basicRemote.sendText(trackingInfoDto.toJson())
                }
            }
        }
    }

}