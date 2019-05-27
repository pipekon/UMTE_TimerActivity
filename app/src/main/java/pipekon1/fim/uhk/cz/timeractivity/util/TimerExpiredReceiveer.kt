package pipekon1.fim.uhk.cz.timeractivity.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import pipekon1.fim.uhk.cz.timeractivity.TimerActivity

class TimerExpiredReceiveer : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        PrefUtil.setTimerState(TimerActivity.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}
