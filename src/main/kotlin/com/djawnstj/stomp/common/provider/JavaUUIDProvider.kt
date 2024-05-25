package com.djawnstj.stomp.common.provider

import java.util.UUID

class JavaUUIDProvider: UUIDProvider {

    override fun generateUuidString(): String = UUID.randomUUID().toString()

}
