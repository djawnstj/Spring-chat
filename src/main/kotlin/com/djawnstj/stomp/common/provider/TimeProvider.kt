package com.djawnstj.stomp.common.provider

interface TimeProvider {
    fun getCurrentTimeMillis(): Long
}
