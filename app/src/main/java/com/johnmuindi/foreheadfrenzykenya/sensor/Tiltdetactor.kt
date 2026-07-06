package com.johnmuindi.foreheadfrenzykenya.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.abs
import kotlin.math.atan2

class TiltDetector(
    context: Context,
    private val onTiltForward: () -> Unit,
    private val onTiltBackward: () -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val tiltThreshold = 35f
    private val calibrationDelayMs = 1000L

    private var startTime = 0L
    private var baselineAngle: Float? = null
    private var isNeutral = true

    fun start() {
        startTime = System.currentTimeMillis()
        baselineAngle = null
        isNeutral = true
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        // X axis is the correct tilt axis when phone is held in landscape on forehead
        val x = event.values[0]
        val z = event.values[2]
        val angle = Math.toDegrees(atan2(x.toDouble(), z.toDouble())).toFloat()

        if (baselineAngle == null) {
            if (System.currentTimeMillis() - startTime > calibrationDelayMs) {
                baselineAngle = angle
            }
            return
        }

        val delta = angle - baselineAngle!!

        when {
            delta > tiltThreshold && isNeutral -> {
                isNeutral = false
                onTiltForward()
            }
            delta < -tiltThreshold && isNeutral -> {
                isNeutral = false
                onTiltBackward()
            }
            abs(delta) < tiltThreshold / 2 -> {
                isNeutral = true
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}