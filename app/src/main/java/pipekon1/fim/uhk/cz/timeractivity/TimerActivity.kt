package pipekon1.fim.uhk.cz.timeractivity

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog


import kotlinx.android.synthetic.main.timer_activity.*
import kotlinx.android.synthetic.main.content_main.*
import pipekon1.fim.uhk.cz.timeractivity.util.NotificationUtil
import pipekon1.fim.uhk.cz.timeractivity.util.PrefUtil
import pipekon1.fim.uhk.cz.timeractivity.util.TimerExpiredReceiveer
import java.util.*
import android.widget.SeekBar



class TimerActivity : AppCompatActivity() {

    companion object {
         fun setAlarm(context: Context, nowSeconds: Long, secondRemaining: Long): Long {
             val wakeUpTime = (nowSeconds + secondRemaining) * 1000
             val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
             val intent = Intent(context, TimerExpiredReceiveer::class.java)
             val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
             alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
             PrefUtil.setAlarmSetTime(nowSeconds, context)
             return wakeUpTime
         }

        fun removeAlarm(context: Context) {
            val intent = Intent(context, TimerExpiredReceiveer::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
        get() = Calendar.getInstance().timeInMillis / 1000
    }

    enum class  TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped
    private var secondsRemaining = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timer_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setIcon(R.drawable.ic_timer)
        supportActionBar?.title = "Timer"


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {

                val seekbarValue = i.toString()

                textView1.setText(seekbarValue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        fab_start.setOnClickListener { v ->
            startTimer()
            timerState = TimerState.Running
        //    setNewTimerLength()
            updateButtons()
        }
        fab_pause.setOnClickListener { v ->
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }
        fab_stop.setOnClickListener { v->
            timer.cancel()
            onTimerFinished()
        }

        confirmButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.setTitle("Dialog")
            builder.setMessage("Opravdu chcete aktivitu ukončit?")

            builder.setPositiveButton("Ano") { dialog, _ ->
                val resultIntent = Intent()
                resultIntent.putExtra("result", "Done!")
                setResult(Activity.RESULT_OK, resultIntent)
                dialog.dismiss()
                finish()
            }

            builder.setNegativeButton("Zrušit") { dialog, _ ->
                dialog.dismiss()
            }

            builder.create().show()
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()

        removeAlarm(this)

        NotificationUtil.hideTimerNotification(this)
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running){
            timer.cancel()
            val wakeUpTime = setAlarm(this, nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(this, wakeUpTime)
        }
        else if(timerState == TimerState.Paused){
            NotificationUtil.showTimerPaused(this)
        }
        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)

    }

    private fun initTimer(){
        //timerState = PrefUtil.getTimerState(this)

   //     var roundsRemaining = seekBarOpak.getProgress()

        if(timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(this)
        if(alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0){
            onTimerFinished()
        }
        else if(timerState == TimerState.Running) {
            startTimer()
        }

  /*      if (secondsRemaining <= 0 && roundsRemaining < 0){
            roundsRemaining -=1
            setNewTimerLength()
            startTimer()
        }*/


       /* if (secondsRemaining <= 0 && roundsRemaining == 0)
            onTimerFinished()
        else if(timerState == TimerState.Running)
            startTimer()*/

        updateButtons()
        updateCountdownUI()

    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped

        setNewTimerLength()

        progress_countdown.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer(){

        timerState = TimerState.Running



        timer = object: CountDownTimer(secondsRemaining*1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

/*
    private fun setNewTimerLength(){
        val initTime = seekBar.getProgress()
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        progress_countdown.max = timerLengthSeconds.toInt()
    }
*/

    private fun setNewTimerLength(){
        val lengthInMinutes = seekBar.getProgress()
        //val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength(){
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private  fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished =  secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        textView_countdown.text = "$minutesUntilFinished:${
            if(secondsStr.length == 2) secondsStr
            else "0" + secondsStr}"
        progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
        }

    private fun updateButtons() {
        when(timerState) {
            TimerState.Running -> {
                fab_start.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = false
            }
            TimerState.Stopped -> {
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
            TimerState.Paused -> {
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
