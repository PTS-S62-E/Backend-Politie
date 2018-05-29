package com.pts6.politie.websockets

import com.pts62.common.finland.communication.RegistrationMovementService
import com.pts62.common.finland.dto.TrackingInfoDto
import com.pts62.common.toJson
import javax.websocket.OnClose
import javax.websocket.OnOpen
import javax.websocket.Session
import javax.websocket.server.ServerEndpoint

@ServerEndpoint("/tracking")
class TrackingSocket {

    @OnOpen
    fun onOpen(session: Session, licensePlate: String) {
        println("User connected")
        sessionLicensePlates.put(session, licensePlate)
        println("${Companion.sessionLicensePlates.size} sessions active")
    }

    @OnClose
    fun onClose(session: Session) {
        val licensePlate = sessionLicensePlates.get(session)
        if (licensePlate != null && moreThanOnce(licensePlate)){
            RegistrationMovementService.getInstance().deleteTracking(licensePlate)
            sessionLicensePlates.remove(session)
        }
        println("User disconnected")
    }

    fun moreThanOnce(licensePlate: String): Boolean {
        var count = 0
        for (pair in sessionLicensePlates) {
            if (pair.value.equals(licensePlate)) count++
        }
        return count > 1
    }

    companion object {
        val sessionLicensePlates = mutableMapOf<Session, String>()

        fun sendMessage(trackingInfoDto: TrackingInfoDto){
            sessionLicensePlates.forEach {
                if(it.value.equals(trackingInfoDto.licensePlate)) {
                    it.key.basicRemote.sendText(trackingInfoDto.toJson())
                }
            }
        }
    }

}