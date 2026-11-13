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
    private val onTiltBackward: () -> Unit,
    private val onCalibrated: () -> Unit = {}
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Raw accelerometer — gravity sensor adds latency which hurts responsiveness
    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // Low-pass filter smooths jitter without adding complex calibration logic
    // Higher alpha = smoother but slightly more lag. 0.85 is a good balance.
    private val alpha = 0.85f
    private val smoothed = FloatArray(3) { 0f }

    private val tiltThreshold = 28f   // degrees to trigger — tune between 20-35
    private val cooldownMs    = 800L  // prevent double-firing
    private val calibDelayMs  = 2000L // wait 2 full seconds for phone to settle on forehead

    private var startTime      = 0L
    private var baselineAngle: Float? = null
    private var isNeutral      = true
    private var lastTiltTime   = 0L
    private var hasCalibrated  = false

    fun start() {
        smoothed.fill(0f)
        startTime     = System.currentTimeMillis()
        baselineAngle = null
        isNeutral     = true
        lastTiltTime  = 0L
        hasCalibrated = false
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return

        // Step 1 — smooth each axis with low-pass filter
        smoothed[0] = alpha * smoothed[0] + (1 - alpha) * event.values[0]
        smoothed[1] = alpha * smoothed[1] + (1 - alpha) * event.values[1]
        smoothed[2] = alpha * smoothed[2] + (1 - alpha) * event.values[2]

        // Step 2 — calculate tilt angle from smoothed X and Z axes
        // X-Z gives forward/backward tilt in locked landscape orientation
        val angle = Math.toDegrees(
            atan2(smoothed[0].toDouble(), smoothed[2].toDouble())
        ).toFloat()

        // Step 3 — wait for phone to fully settle before locking baseline
        if (!hasCalibrated) {
            if (System.currentTimeMillis() - startTime >= calibDelayMs) {
                baselineAngle = angle
                hasCalibrated = true
                onCalibrated()
            }
            return
        }

        // Step 4 — detect tilt relative to baseline
        val baseline = baselineAngle ?: return
        val delta    = angle - baseline
        val now      = System.currentTimeMillis()
        val reArmZone = tiltThreshold * 0.35f // ~10° dead zone at centre

        when {
            delta > tiltThreshold
                    && isNeutral
                    && (now - lastTiltTime) > cooldownMs -> {
                isNeutral    = false
                lastTiltTime = now
                onTiltForward()
            }
            delta < -tiltThreshold
                    && isNeutral
                    && (now - lastTiltTime) > cooldownMs -> {
                isNeutral    = false
                lastTiltTime = now
                onTiltBackward()
            }
            abs(delta) < reArmZone -> {
                isNeutral = true
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}