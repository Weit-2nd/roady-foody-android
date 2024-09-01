package com.weit2nd.presentation.util

import com.weit2nd.domain.model.Coordinate
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val EARTH_RADIUS = 6371000.0

/**
 * 두 좌표 사이의 거리를 미터로 반환 합니다.
 */
fun getDistanceMeter(
    start: Coordinate,
    end: Coordinate,
): Int {
    val latitudeGap = Math.toRadians(start.latitude - end.latitude)
    val longitudeGap = Math.toRadians(start.longitude - end.longitude)

    val haversineLat = sin(latitudeGap / 2).pow(2)
    val haversineLng = sin(longitudeGap / 2).pow(2)
    val haversineFormula =
        haversineLat +
            (cos(Math.toRadians(start.latitude)) * cos(Math.toRadians(end.latitude)) * haversineLng)

    val angularDistance = 2 * atan2(sqrt(haversineFormula), sqrt(1 - haversineFormula))

    return (EARTH_RADIUS * angularDistance).toInt() // 거리 (미터)
}
