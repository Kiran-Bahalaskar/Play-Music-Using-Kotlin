package com.example.playmusicusingkotlin

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var mp: MediaPlayer
    private var totalTime : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mp = MediaPlayer.create(this, R.raw.music)
        mp.isLooping = true
        mp.setVolume(0.5f, 0.5f)
        totalTime = mp.duration

        // Volume Bar
        volumeBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                {
                    var volumeNum = progress / 100.0f
                    mp.setVolume(volumeNum, volumeNum)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        positionBar.max = totalTime
        positionBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                {
                    mp.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        Thread(Runnable {
            while (mp !=null)
            {
                try {
                    var msg = Message()
                    msg.what = mp.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                }
                catch (e: InterruptedException)
                {

                }
            }
        }).start()
    }

    @SuppressLint("HandlerLeak")
    var handler = object : Handler()
    {
        override fun handleMessage(msg: Message) {
            var currentPostion = msg.what

            // Update positionbar
            positionBar.progress = currentPostion

            // Update labels
            var elspedTime = createTimeLable(currentPostion)
            elapsedTimeLabel.text = elspedTime

            var remainingTime = createTimeLable(totalTime - currentPostion)
            remainingTimeLabel.text = "-$remainingTime"
        }
    }

    fun createTimeLable(time: Int): String
    {
        var timeLabel = ""
        var min = time / 100 / 60
        var sec = time / 100 % 60

        timeLabel = "$min:"
        if (sec < 10)
        {
            timeLabel += "0"
        }
        else
        {
            timeLabel += sec
        }
        return timeLabel
    }

    fun playBtnClick(v: View)
    {
        if (mp.isPlaying)
        {
            //stop
            mp.pause()
            playBtn.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp)
        }
        else
        {
            //start
            mp.start()
            playBtn.setBackgroundResource(R.drawable.ic_stop_black_24dp)
        }
    }
}
