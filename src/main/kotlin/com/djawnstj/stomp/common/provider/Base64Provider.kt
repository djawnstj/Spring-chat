package com.djawnstj.stomp.common.provider

interface Base64Provider {

    fun decode(src: String): ByteArray

}
