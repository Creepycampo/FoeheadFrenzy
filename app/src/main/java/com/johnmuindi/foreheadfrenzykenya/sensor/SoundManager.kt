package com.johnmuindi.foreheadfrenzykenya.sensor

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.johnmuindi.foreheadfrenzykenya.R

class SoundManager(context: Context) {

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(3)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private val correctSound = soundPool.load(context, R.raw.correct, 1)
    private val skipSound = soundPool.load(context, R.raw.skip, 1)
    private val roundEndSound = soundPool.load(context, R.raw.round_end, 1)

    fun playCorrect() = soundPool.play(correctSound, 1f, 1f, 0, 0, 1f)
    fun playSkip() = soundPool.play(skipSound, 1f, 1f, 0, 0, 1f)
    fun playRoundEnd() = soundPool.play(roundEndSound, 1f, 1f, 0, 0, 1f)

    fun release() = soundPool.release()
}