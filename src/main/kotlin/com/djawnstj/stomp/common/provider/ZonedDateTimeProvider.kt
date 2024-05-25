package com.djawnstj.stomp.common.provider

import java.time.ZonedDateTime

class ZonedDateTimeProvider: TimeProvider {

    override fun getCurrentTimeMillis(): Long = ZonedDateTime.now().toInstant().toEpochMilli()

}
