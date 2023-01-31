package com.example.desibel

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

class NoiseCancel {
    val noiseLevel: Double
        @SuppressLint("MissingPermission")
        get() {
            var bufferSize = AudioRecord.getMinBufferSize(
                44100,
                AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT
            )
            bufferSize *= 4
            val recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                44100, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT, bufferSize
            )
            val data = ShortArray(bufferSize)
            var average = 0.0
            recorder.apply {
                startRecording()
                read(data,0,bufferSize)
                stop()
            }
            for (s in data) {
                if (s > 0) {
                    average += abs(s.toInt()).toDouble()
                } else {
                    bufferSize--
                }
            }
            recorder.release()
            val audioLevel = (average / bufferSize)
            val pressureLevel = audioLevel * AUDIO_TO_PRESSURE_CONVERTER_VALUE
            return 20 * log10(pressureLevel / MINIMUM_PASCAL_REFERENCE)
        }

    companion object {
        const val MINIMUM_PASCAL_REFERENCE = 0.00002
        private const val MAX_DB_PHONE_MICROPHONE = 105.0
        private val MAX_MICROPHONE_DB_TO_PASCAL = 10.0.pow((MAX_DB_PHONE_MICROPHONE / 20)) * MINIMUM_PASCAL_REFERENCE
        val AUDIO_TO_PRESSURE_CONVERTER_VALUE = MAX_MICROPHONE_DB_TO_PASCAL / Short.MAX_VALUE
    }
    
}